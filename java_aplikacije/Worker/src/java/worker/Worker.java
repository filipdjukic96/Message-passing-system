/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package worker;

import entities.*;
import entitiesPrivate.*;
import java.io.IOException;
import javax.persistence.*;
import javax.annotation.Resource;
import javax.jms.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Filip Djukic
 */
public class Worker {

    @Resource(lookup = "MyTopic")
    static Topic topic;
    @Resource(lookup = "jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;

    public Scanner scanner;

    public EntityManagerFactory emf;
    public EntityManager em;

    public EntityManagerFactory emfPrivate;
    public EntityManager emPrivate;

    public JMSContext context;
    public JMSProducer producer;

    public Worker() {
        emf = Persistence.createEntityManagerFactory("WorkerPU");
        em = emf.createEntityManager();

        emfPrivate = Persistence.createEntityManagerFactory("WorkerPU2");
        emPrivate = emfPrivate.createEntityManager();

        scanner = new Scanner(System.in);

        context = connectionFactory.createContext();
        context.setClientID("Worker");
        producer = context.createProducer();

  
    }


    public void work() {
        System.out.println("Aplikacija Radnik");

        boolean working = true;

        while (working) {
            System.out.println("");
            System.out.println("1. Provjera prozivoda");
            System.out.println("2. Proizvodnja artikla");
            System.out.println("3. Promjena cijene artikla");
            System.out.println("4. Kraj");
            System.out.println("");
            int choice = scanner.nextInt();
            System.out.println("");
          /*  try {

            Runtime.getRuntime().exec("cls");
            } catch (IOException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }*/

            while (choice < 1 && choice > 4) {
                System.out.println("");
                System.out.println("Pogresan unos!Unesite ponovo");
                System.out.println("");
                continue;
            }

            if (choice == 1) {
                displayItems();
            } else if (choice == 2) {
                createItem();
            } else if (choice == 3) {
                changeItemPrice();
            } else {
                working = false;
            }

        }

    }

    private void displayItems() {
        Query query = emPrivate.createQuery("SELECT p FROM Product p");
        List<Product> products = query.getResultList();

        System.out.println(" ");
        System.out.println("idProduct \t Naziv \t Tip \t Cijena \t VrijemeProizvodnje");
        for (Product p : products) {
            System.out.print(p.getIdProduct() + "\t");
            System.out.print(p.getName() + "\t");
            System.out.print(p.getType() + "\t");
            System.out.print(p.getPrice() + "\t");
            System.out.print(p.getManufactureTime());
            System.out.println("");
        }

        System.out.println(" ");
    }

    private void createItem() {

        try {
            //unos id-a
            int productId = enterProductId();
            //unos kolicine
            int quantity = enterQuantity();
            //vrijeme proizvodnje
            int manufactureTime = getManufactureTime(productId);
            //dohvatanje radnje
            int storeId = getRandomStore();
           //int storeId=4;//za testiranje
            System.out.println("Proizvodnja u prodavnicu:"+storeId);
            //proizvodnja
            Thread.sleep(manufactureTime);

            //slanje poruke menadzeru
            sendMessageToManager(productId, storeId, quantity);

        } catch (InterruptedException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JMSException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private int enterProductId() {
        System.out.println("");
        System.out.println("Unesite id proizvoda");
        System.out.println("");
        int productId = scanner.nextInt();
        System.out.println("");
        //provjera validnosti
        while (true) {
            Product product = emPrivate.find(Product.class, productId);
            if (product != null) {
                break;
            }
            System.out.println("");
            System.out.println("Pogresan unos,unesite id ponovo");
            System.out.println("");
            productId = scanner.nextInt();
            System.out.println("");
        }
        System.out.println("");
        System.out.println("");
        return productId;
    }

    private int enterQuantity() {
        System.out.println("");
        System.out.println("Unesite kolicinu proizvoda");
        System.out.println("");
        int quantity = scanner.nextInt();
        System.out.println("");

        while (true) {
            if (quantity > 0) {
                break;
            }
            System.out.println("");
            System.out.println("Nevalidna kolicina,unesite ponovo");
            System.out.println("");
            quantity = scanner.nextInt();
            System.out.println("");
        }
        System.out.println("");
        System.out.println("");
        return quantity;
    }

    private int getManufactureTime(int productId) {
        Product product = emPrivate.find(Product.class, productId);
        return product.getManufactureTime();

    }

    private int getRandomStore() {
        TypedQuery<Integer> query = em.createQuery("SELECT MAX(s.idStore) FROM Store s", Integer.class);
        int maxId = query.getSingleResult();

        int storeId = (int) (Math.random() * maxId);

        //ako je u medjuvremenu izbrisana
        while (em.find(Store.class, storeId) == null) {
            storeId = (int) (Math.random() * maxId);
        }

        return storeId;
    }

    private void sendMessageToManager(int productId, int storeId, int quantity) throws JMSException {
        TextMessage txt = context.createTextMessage();

        txt.setIntProperty("idProduct", productId);
        txt.setIntProperty("quantity", quantity);
        txt.setIntProperty("idStore", storeId);

        //1-manager
        txt.setIntProperty("receiver", 1);
        txt.setIntProperty("function", 3);

        producer.send(topic, txt);
    }

    private void changeItemPrice() {

        try {
            //unos id-a
            int productId = enterProductId();
            
            //unos nove cijene
            double price = enterNewPrice();
            
            //promjena interno
            changePricePrivate(productId, price);
            
            //promjena u bazi
            changePrice(productId, price);
            
            //obavijesti menadzere
            sendMessageToAllManagers(productId, price);
            
        } catch (JMSException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private double enterNewPrice() {
        System.out.println("");
        System.out.println("Unesite novu cijenu");
        System.out.println("");
        double price = scanner.nextDouble();
        System.out.println("");

        while (true) {
            if (price > 0) {
                break;
            }
            System.out.println("");
            System.out.println("Pogresan unos cijene,unesite ponovo");
            System.out.println("");
            price = scanner.nextDouble();
            System.out.println("");
        }
        return price;
    }

    private void changePrice(int productId, double price) {
        Article article = em.find(Article.class, productId);

        em.getTransaction().begin();
        article.setPrice(price);
        em.getTransaction().commit();
        //em.refresh(article);
    }

    private void changePricePrivate(int productId, double price) {
        Product product = emPrivate.find(Product.class, productId);

        emPrivate.getTransaction().begin();
        product.setPrice(price);
        emPrivate.getTransaction().commit();
        //em.refresh(product);
    }

    private void sendMessageToAllManagers(int productId, double price) throws JMSException {
        TypedQuery<Integer> query = em.createQuery("SELECT MAX(s.idStore) FROM Store s", Integer.class);
        int maxId = query.getSingleResult();
        System.out.println("IdstoreMAX:"+maxId);
        TextMessage txt=context.createTextMessage();
        
        txt.setIntProperty("function", 4);
        txt.setIntProperty("receiver", 1);
        txt.setIntProperty("idProduct", productId);
        txt.setDoubleProperty("price", price);
        
        //posalji menadzeru svake prodavnice
        for(int i=1;i<=maxId;i++){
            if(em.find(Store.class,i)==null) continue;
            txt.setIntProperty("idStore", i);
            producer.send(topic,txt);
        }
        
    }

    public static void main(String[] args) {
        Worker worker=new Worker();
        worker.work();
    }

}

