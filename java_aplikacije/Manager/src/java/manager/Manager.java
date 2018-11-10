/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import entities.Article;
import entities.Booking;
import entities.Onstock;
import entities.OnstockPK;
import entities.Store;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Filip Djukic
 */
public class Manager {

    @Resource(lookup = "MyTopic")
    static Topic topic;

    @Resource(lookup = "jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;

    private int storeId;
    private String storeName;

    private JMSContext context;
    private JMSConsumer consumer;
   
    
    private JMSProducer producer;

    private EntityManagerFactory emf;
    private EntityManager em;

    private static int idNewStore=1;
    /**
     * @param args the command line arguments
     */
    public Manager(int storeId) {
        this.storeId = storeId;
        //this.storeName = storeName;

        context = connectionFactory.createContext();
        context.setClientID(storeId + storeName);

        producer = context.createProducer();
        consumer = context.createDurableConsumer(topic, "Manager" + storeId , "idStore = " + storeId + " AND receiver = 1", false);

        
        emf = Persistence.createEntityManagerFactory("ManagerPU");
        em = emf.createEntityManager();

        //ako nema radnje,dodaj se
        Store store = em.find(Store.class, storeId);
        if (store == null) {
            store = new Store(storeId, "Radnja"+idNewStore++);
            em.getTransaction().begin();
            em.persist(store);
            em.getTransaction().commit();
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        Manager manager=new Manager(4);
        manager.work();
    }


    public void work() {
        System.out.println("Aplikacija menadzer:"+storeId);
        while (true) {

            Message msg = consumer.receive();
            if (msg instanceof TextMessage) {
                try {
                    TextMessage txt = (TextMessage) msg;
                    int function = txt.getIntProperty("function");
                    //provjera stanja
                    if (function == 1) {
                        int idProduct = txt.getIntProperty("idProduct");
                        int quantity = txt.getIntProperty("quantity");
                        boolean wrapped = txt.getBooleanProperty("wrapped");
                        int idVendor = txt.getIntProperty("idVendor");
                        
                        //id prodavnice kojoj odgovara
                        int responseStore=txt.getIntProperty("responseStore");
                        
                        checkArticleWrapped(idProduct, quantity, wrapped, idVendor,responseStore);
                    } else if (function == 2) {
                        //rezervacija
                        int idProduct=txt.getIntProperty("idProduct");
                        int quantity=txt.getIntProperty("quantity");
                        int idVendor=txt.getIntProperty("idVendor");
                        String contact=txt.getStringProperty("contact");
                        
                        //id prod kojoj odgovara
                        int responseStore=txt.getIntProperty("responseStore");
                        
                        handleBooking(idProduct,quantity,idVendor,contact,responseStore); 
                    } else if (function == 3) {
                        //proizvodnja artikala
                        int idProduct = txt.getIntProperty("idProduct");
                        int quantity = txt.getIntProperty("quantity");
                        
                        produceArticle(idProduct, quantity);
                    } else if (function == 4) {
                        //promjena cijene
                        int idProduct = txt.getIntProperty("idProduct");
                        double price = txt.getDoubleProperty("price");
                       
                        changeArticlePrice(idProduct, price);
                    } else {
                        continue;
                    }

                } catch (JMSException ex) {
                    Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    private void checkArticleWrapped(int idProduct, int quantity, boolean wrapped, int idVendor,int responseStore) {
        
        //System.out.println("Menadzer prodavnice "+storeId+" zaprimio poruku o upitu stanja");
       // System.out.println("Trazi se stanje:"+wrapped);
       // System.out.println("Trazena kolicina:"+quantity);
        try {
            Onstock stock=em.find(Onstock.class,new OnstockPK(idProduct,storeId));
            em.refresh(stock);
            int quantityInStore=stock.getQuantity();
            int wrappedCounter=0,unwrappedCounter=0;
            
            for(int i=0;i<quantityInStore;i++){
                int rand=(int)(Math.random()*100);
                //System.out.println("broj:"+rand);
                if(rand<70){
                    unwrappedCounter++;
                }else{
                    wrappedCounter++;
                }
            }
            
            //System.out.println("Zapakovani:"+wrappedCounter);
            //System.out.println("Nezapakovani:"+unwrappedCounter);
            
            int requiredQuantity=(wrapped==true)?wrappedCounter:unwrappedCounter;
            
            TextMessage txt=context.createTextMessage();
            
            if(requiredQuantity>=quantity){
                //System.out.println("Menadzer odgovara pozitivno");
                txt.setBooleanProperty("stateResponse", true);
            }else{
               // System.out.println("Menadzer odgovara negativno");
                txt.setBooleanProperty("stateResponse", false);
            }   
            
            
            //pripreme poruke za slanje prodavcu(vendor) koji je trazio upit(njegov store je responseStore)
            txt.setIntProperty("idStore", responseStore);
            txt.setIntProperty("idProduct", idProduct);
            txt.setIntProperty("function", 1);
            txt.setIntProperty("idVendor", idVendor);
            
            //receiver=-1 salje se za prodavca
            txt.setIntProperty("receiver", -1);
            
            producer.send(topic,txt);
        } catch (JMSException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //System.out.println("Menadzer prodavnice "+storeId+" poslao odgovor za upit o stanju");
    }

    //PITATI ZA AUTOINCREMENT ZA ID!!!!
    private void handleBooking(int idProduct,int quantity,int idVendor,String contact,int responseStore){
        //System.out.println("Menadzer prodavnice "+storeId+" primio poruku o pravljenju rezervacije");
        try {
            Article article=em.find(Article.class, idProduct);
            Store store=em.find(Store.class,storeId);
            em.refresh(article);
            em.refresh(store);
            
            Date date=new Date();
            
            //napravi rezervaciju
            Booking booking=new Booking(quantity,contact,date);
            booking.setIdArticle(article);
            booking.setIdStore(store);
            
            em.getTransaction().begin();
            em.persist(booking);
            em.getTransaction().commit();
            
            TextMessage txt=context.createTextMessage();
            
            txt.setIntProperty("idBooking", booking.getIdBooking());
            txt.setIntProperty("idStore",responseStore);//slanje na responseStore(prodavnica od prodavca koji je trazio rezervaciju)
            txt.setIntProperty("function", 2);
            txt.setIntProperty("idProduct", idProduct);
            txt.setIntProperty("idVendor", idVendor);
            
            //slanje poruke prodavcu da je napravljena rez
            txt.setIntProperty("receiver", -1);
            producer.send(topic, txt);
        } catch (JMSException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //System.out.println("Menadzer prodavnice "+storeId+" poslao poruku o izvrsenoj rezervaciji");
    }
    
    //testirano
    private void produceArticle(int idProduct, int quantity) {
        //System.out.println("Menadzer dobio nalog za proizvodnju proizvoda:"+idProduct+",kolicina:"+quantity);
        Onstock stock = em.find(Onstock.class, new OnstockPK(idProduct,storeId));

        //ima na stanju u ovoj prodavnici
        if (stock != null) {
            em.getTransaction().begin();
            stock.setQuantity(stock.getQuantity() + quantity);
            em.getTransaction().commit();

        } else {//nema na stanju,kreiraj novo
            stock = new Onstock(new OnstockPK(idProduct, storeId), quantity);
            em.getTransaction().begin();
            em.persist(stock);
            em.getTransaction().commit();
        }
        em.refresh(stock);
        //System.out.println("Menadzer proizveo proizvod:"+idProduct+",kolicina:"+quantity);
    }

    //testirano
    private void changeArticlePrice(int idProduct, double price) {
        //PRAZNA FJA ZASAD,PITATI TAMARU!!
        //System.out.println("Primljena poruka o promjeni cijene");
    }


}
