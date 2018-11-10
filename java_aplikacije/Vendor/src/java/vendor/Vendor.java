/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vendor;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ConnectionFactory;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entities.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Filip Djukic
 */
public class Vendor {

    @Resource(lookup = "MyTopic")
    static Topic topic;
    @Resource(lookup = "jms/__defaultConnectionFactory")
    static ConnectionFactory connectionFactory;

    public EntityManagerFactory emf;
    public EntityManager em;

    private int storeId;
    private int vendorId;
    private Scanner scanner;

    private JMSContext context;
    private JMSProducer producer;
    private JMSConsumer consumer;

    //posljednje trazeno stanje proizvoda
    //0-svejedno,1-wrapped,2-unwrapped
    private int lastRequestedWrappedState;

    //private JMSConsumer consumerState;
    //private JMSConsumer consumerBooking;
    public Vendor(int storeId, int vendorId) {
        this.storeId = storeId;
        this.vendorId = vendorId;
        scanner = new Scanner(System.in);

        emf = Persistence.createEntityManagerFactory("VendorPU");
        em = emf.createEntityManager();

        context = connectionFactory.createContext();
        context.setClientID("Vendor:" + vendorId + ",Store:" + storeId);
        producer = context.createProducer();
        //da li treba Durable????
        consumer = context.createConsumer(topic);

    }

    public static void main(String[] args) {
        Vendor vendor = new Vendor(4, 15);
        vendor.work();
    }

    public void work() {

        boolean working = true;
        while (working) {
            System.out.println("Prodavac:" + vendorId + ",prodavnica:" + storeId);
            System.out.println("1. Kupovina proizvoda");
            System.out.println("2. Kupovina rezervisanog proizvoda");
            System.out.println("3. Kraj");
            System.out.println("");
            //System.out.println("");
            int choice = scanner.nextInt();
            System.out.println("");
            if (choice == 1) {
                handleUnbookedArticle();
            } else if (choice == 2) {
                handleBookedArticle();
            } else if (choice == 3) {
                return;
            } else {
                System.out.println("Pogresan unos!Unesite ponovo!");
                System.out.println("");
            }

        }
    }

    //za rezervisan artikal
    private void handleBookedArticle() {
        System.out.println("1 .Unos id-a rezervacije");
        System.out.println("2. Povratak na glavni meni");
        int choice = scanner.nextInt();

        if (choice < 1 && choice > 2) {
            System.out.println("Pogresan unos!");
            return;
        }

        if (choice == 2) {
            return;
        }

        System.out.println("Unesite id rezervacije koju zelite da podignete:");
        int idBooking = scanner.nextInt();

        boolean bookingExists = checkBooking(idBooking);

        //rezervacija ne postoji
        if (bookingExists == false) {
            System.out.println("Rezervacija koju ste unijeli nije validna!");
            return;
        }

        boolean bookingOutdated = bookingOutdated(idBooking);

        if (bookingOutdated == true) {
            System.out.println("Rezervacija koju se unijeli je istekla!");
            return;
        }

        //sve ok,rezervacija validna
        bookingTakeover(idBooking);

    }

    //provjerava validnost rezetvacije,vraca na stanje ako je istekla
    private boolean bookingOutdated(int idBooking) {
        Booking booking = em.find(Booking.class, idBooking);

        if (booking != null) {
            em.refresh(booking);
        } else {
            return false;
        }

        Date currentDate = new Date();
        Date bookingDate = booking.getDate();

        long secs = (currentDate.getTime() - bookingDate.getTime()) / 1000;

        if (secs < 172800) {
            //rezervacija validna,nije istekla
            return false;
        }

        //istekla rezervacija,uklanja se i vraca na stanje kolicina
        int idProduct = booking.getIdArticle().getIdArticle();
        Onstock stock = em.find(Onstock.class, new OnstockPK(idProduct, storeId));
        em.refresh(stock);
        //nova kolicina na stanju u prodavnici
        int newQuantity = stock.getQuantity() + booking.getQuantity();

        em.getTransaction().begin();
        em.remove(booking);
        stock.setQuantity(newQuantity);
        em.getTransaction().commit();

        return true;
    }

    private boolean checkBooking(int idBooking) {
        Booking booking = em.find(Booking.class, idBooking);

        if (booking == null) {
            return false;
        }

        if (booking.getIdStore().getIdStore() != storeId) {
            return false;
        }

        return true;

    }

    //preuzimanje rezervacije
    private void bookingTakeover(int idBooking) {
        Booking booking = em.find(Booking.class, idBooking);

        int quantity = booking.getQuantity();
        int idProduct = booking.getIdArticle().getIdArticle();

        //uklanjanje rezervacije
        em.getTransaction().begin();
        em.remove(booking);
        em.getTransaction().commit();

        //azuriranje prometa
        updateTrading(idProduct, quantity);

    }

    private void updateTrading(int idProduct, int quantity) {
        Article article = em.find(Article.class, idProduct);
        em.refresh(article);
        double articlePrice = article.getPrice();
        //System.out.println("Azuriranje prometa");
        Query query = em.createQuery("SELECT t FROM Trading t WHERE t.store.idStore=?1");
        query.setParameter(1, storeId);

        List<Trading> myTrading = query.getResultList();

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        Date todaysDate = new Date();

        //System.out.println("Danasnji datum:" + dateFormat.format(todaysDate));

        for (Trading t : myTrading) {
            Date tradingDate = t.getTradingPK().getDate();

            if (todaysDate.getDay() == tradingDate.getDay()) {
                if (todaysDate.getMonth() == tradingDate.getMonth()) {
                    if (todaysDate.getYear() == tradingDate.getYear()) {
                       
                        em.getTransaction().begin();
                        t.setQuantity(t.getQuantity() + quantity);
                        double totalSum = t.getTotalSum() + quantity * articlePrice;
                        t.setTotalSum(totalSum);
                        em.getTransaction().commit();
                       // System.out.println("Pronadjen dnevni promet,datum:" + dateFormat.format(tradingDate));
                        return;

                     
                    }
                }
            }
        }

        //promet nije nadjen,kreiranje novog 'dnevnog' prometa
        double totalSum = quantity * articlePrice;
        Trading trading = new Trading(new TradingPK(storeId, todaysDate), quantity, totalSum);

        em.getTransaction().begin();
        em.persist(trading);
        em.getTransaction().commit();
        //System.out.println("Stvoren novi dnevni promet");
    }

    //za nerezervisan artikal
    private void handleUnbookedArticle() {

        //prikaz proizvoda
        boolean display = displayItemsForCustomer();
        if (display == false) {
            //povratak nazad izabran
            return;
        }

        int idProduct = enterArticleId();
        if (idProduct < 0) {
            //izabran povratak nazad
            return;
        }

        int quantity = enterArticleQuantity();
        if (quantity < 0) {
            //izabran povratak nazad
            return;
        }

        boolean enoughArticles = checkEnoughArticles(idProduct, quantity);

        if (enoughArticles == true) {
            //System.out.println("!!Dovoljno artikala!!");
            //ima proizvoda na stanju,obavi kupovinu
            purchaseArticles(idProduct, quantity);
        } else {
            //System.out.println("!!!Nedovoljno artikala!!!");
            handleArticleShortage(idProduct, quantity);

        }

    }

    //fja za obradu slucaja kada nema dovoljno artikala u trazenoj kolicini u ovoj prodavnici
    private void handleArticleShortage(int idProduct, int quantity) {

        //kolicina na stanju u ovoj prodavnici
        int onStock = getQuantityInStore(idProduct);

        while (true) {
            System.out.println("");
            System.out.println("Nema dovoljno artikala u datom stanju u prodavnici,izaberite opciju:");
            System.out.println("1. Kupovina raspolozivog broja artikala");
            System.out.println("2. Pravljenje rezervacije");
            System.out.println("");
            int choice = scanner.nextInt();
            System.out.println("");
            if (choice == 1) {//kupovina raspolozivog broja artikala u ovoj prodavnici
                purchaseArticles(idProduct, onStock);
                return;
            } else if (choice == 2) {//pravljenje rezervacije
                createNewBooking(idProduct, quantity);
                return;
            } else {
                System.out.println("");
                System.out.println("Niste unijeli odgovarajucu opciju!!");
                System.out.println("1. Ponovni unos");
                System.out.println("2. Povratak na glavni meni");
                System.out.println("");
                choice = scanner.nextInt();
                System.out.println("");
                if (choice == 1) {
                    continue;
                } else {
                    return;
                }

            }
        }
    }

    private boolean displayItemsForCustomer() {
        System.out.println("Kakav prikaz proizvoda zelite?");
        System.out.println("1. Po tipu");
        System.out.println("2. Po imenu");
        System.out.println("3. Povratak nazad");
        System.out.println("");
        int choice = scanner.nextInt();
        System.out.println("");
        if (choice == 1) {
            return displayItemsByType();
           
        } else if (choice == 2) {
            return displayItemsByName();
           
        } else {
            return false;
        }

    }

    private boolean displayItemsByType() {
        try {
            //System.out.println("");
            System.out.println("Unesite tip:");
            System.out.println("");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String type = br.readLine();

            Query query = em.createQuery("SELECT a FROM Article a WHERE a.type=?1");
            query.setParameter(1, type);

            List<Article> articles = query.getResultList();

            if(articles.isEmpty()){
                System.out.println("Ne postoje artikli navedenog tipa!");
                return false;
            }
            
           // System.out.println("");
            System.out.println("");
            //System.out.println("Id\tIme\t\t\t\tTip\tCijena\t");

            for (Article a : articles) {
                em.refresh(a);
                System.out.print(a.getIdArticle() + "\t");
                System.out.print(a.getName() + "\t\t");
                System.out.print(a.getType() + "\t");
                System.out.print(a.getPrice() + "\t");
                System.out.println("");
            }

            //System.out.println("");
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Vendor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private boolean displayItemsByName() {
        try {
            //System.out.println("");
            System.out.println("Unesite ime:");
            System.out.println("");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String name = br.readLine();

            Query query = em.createQuery("SELECT a FROM Article a WHERE a.name=?1");
            query.setParameter(1, name);

            List<Article> articles = query.getResultList();

            if(articles.isEmpty()){
                System.out.println("Ne postoje artikli navedenog imena!");
                return false;
            }
            
           // System.out.println("");
            System.out.println("");
            // System.out.println("Id\tIme\t\t\t\tTip\tCijena\t");

            for (Article a : articles) {
                em.refresh(a);
                System.out.print(a.getIdArticle() + "\t");
                System.out.print(a.getName() + "\t\t");
                System.out.print(a.getType() + "\t");
                System.out.print(a.getPrice() + "\t");
                System.out.println("");
            }

            //System.out.println("");
            System.out.println("");

        } catch (IOException ex) {
            Logger.getLogger(Vendor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    private int enterArticleId() {

        while (true) {
            System.out.println("");
            System.out.println("Unesite id zeljenog artikla");
            System.out.println("");
            int idProduct = scanner.nextInt();
            System.out.println("");
            Article article = em.find(Article.class, idProduct);
            em.refresh(article);

            if (article == null) {
                System.out.println("");
                System.out.println("Artikal ne postoji!");
                System.out.println("Izaberite opciju:");
                System.out.println("1. Ponovni unos");
                System.out.println("2. Povratak nazad");
                System.out.println("");
                int choice = scanner.nextInt();

                if (choice == 2) {
                    return -1;
                }
                //izabran ponovi unos
                continue;

            }
            System.out.println("");
            System.out.println("");
            return idProduct;
        }
    }

    private int enterArticleQuantity() {

        while (true) {
            System.out.println("");
            System.out.println("Unesite kolicinu");
            System.out.println("");
            int quantity = scanner.nextInt();

            if (quantity > 0) {
                System.out.println("");
                System.out.println("");
                return quantity;
            }
            System.out.println("");
            System.out.println("Pogresan unos kolicine!");
            System.out.println("Izaberite opciju:");
            System.out.println("1. Ponovni unos");
            System.out.println("2. Povratak nazad");
            System.out.println("");
            int choice = scanner.nextInt();

            if (choice == 2) {
                return -1;
            }
            //izabran ponovi unos
            continue;

        }

    }

    private int getQuantityInStore(int idProduct) {
        //provjera zastarjelih rezervacija za ovaj proizvod u ovoj prodavnici

        Query query = em.createQuery("SELECT b FROM Booking b WHERE b.idArticle.idArticle=?1 AND b.idStore.idStore=?2");
        query.setParameter(1, idProduct);
        query.setParameter(2, storeId);

        List<Booking> bookings = query.getResultList();

        //trenutno stanje ovog proizvoda u ovoj prodavnici
        //Onstock stock = em.find(Onstock.class, new OnstockPK(idProduct, storeId));
        for (Booking b : bookings) {
            //za svaku rezervaciju ovog proizvoda
            //provjera da li je zastarila i azuriranje stanja
            bookingOutdated(b.getIdBooking());
        }

        Onstock stock = em.find(Onstock.class, new OnstockPK(idProduct, storeId));

        if (stock == null) {
            return 0;
        }

        em.refresh(stock);

        return stock.getQuantity();

    }

    private boolean checkArticleState(int idProduct, int quantity, int wrap, int idOfStore) {
        try {

            TextMessage txt = context.createTextMessage();
            txt.setIntProperty("idVendor", vendorId);
            txt.setIntProperty("idProduct", idProduct);
            txt.setIntProperty("quantity", quantity);
            txt.setIntProperty("idStore", idOfStore);
            txt.setIntProperty("function", 1);
            txt.setIntProperty("receiver", 1);

            txt.setIntProperty("responseStore", storeId);

            if (wrap == 1) {
                txt.setBooleanProperty("wrapped", true);
                //lastRequestedWrappedState=1;//wrapped
            } else {
                txt.setBooleanProperty("wrapped", false);
                //lastRequestedWrappedState=2;//unwrapped
            }

            //slanje poruke menadzeru za upit o stanju
            producer.send(topic, txt);

            String selection = "idProduct = " + idProduct + " AND idStore = " + storeId + " AND function = 1 AND receiver = -1 AND idVendor = " + vendorId;
            JMSConsumer stateConsumer = context.createDurableConsumer(topic, "stateCheck" + idProduct, selection, false);

            Message msg = stateConsumer.receive();
            if (msg instanceof TextMessage) {
                TextMessage txtResponse = (TextMessage) msg;
                boolean response = txtResponse.getBooleanProperty("stateResponse");
                stateConsumer.close();
                //System.out.println("Menadzer odgovorio" + response);
                return response;
            }

            stateConsumer.close();
            return false;

        } catch (JMSException ex) {
            Logger.getLogger(Vendor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    private boolean checkEnoughArticles(int idProduct, int quantity) {
        while (true) {
            System.out.println("");
            System.out.println("Izaberite opciju:");
            System.out.println("1. Provjera stanja proizvoda");
            System.out.println("2. Stanje proizvoda nije bitno");
            System.out.println("");
            int choice = scanner.nextInt();
            System.out.println("");
            //stanje mu nije bitno
            if (choice == 2) {
                lastRequestedWrappedState = 0;//svejedno mu je
                int quantityInStore = getQuantityInStore(idProduct);
                if (quantityInStore >= quantity) {
                    return true;
                } else {
                    return false;
                }

            } //stanje bitno,obraca se menadzeru,on vraca ima li na stanju
            else if (choice == 1) {
                System.out.println("");
                System.out.println("Unesite stanje");
                System.out.println("1. Zapakovan");
                System.out.println("2. Otpakovan");
                System.out.println("");
                int wrap = scanner.nextInt();
                System.out.println("");
                lastRequestedWrappedState = wrap;

                //vrsimo upit kod naseg menadzera,pa je id prodavnice cijem menadzeru saljemo == storeId
                return checkArticleState(idProduct, quantity, wrap, storeId);
            } else {
                System.out.println("Pogresan unos opcije!");
                System.out.println("Unesite ponovo:");
                System.out.println("");
                continue;
            }
        }

    }

    private void purchaseArticles(int idProduct, int quantity) {
        //obavljanje kupovine
        Onstock stock = em.find(Onstock.class, new OnstockPK(idProduct, storeId));
        if (stock != null) {
            em.refresh(stock);
            int newQuantity = stock.getQuantity() - quantity;

            em.getTransaction().begin();
            stock.setQuantity(newQuantity);
            em.getTransaction().commit();

            System.out.println("Uspjesno izvrsena kupovina");

            //azuriranje dnevnog prometa
            updateTrading(idProduct, quantity);
        }
    }

    //fja za trazenje prodavnice koja na stanju ima trazenu kolicinu artikla
    //i odabir neke od njih
    private int selectStore(int idProduct, int quantity) {
        Query query = em.createQuery("SELECT s.store FROM Onstock s WHERE s.quantity>=?1 AND s.onstockPK.idArticle=?2 AND s.onstockPK.idStore<>?3");
        query.setParameter(1, quantity);
        query.setParameter(2, idProduct);
        query.setParameter(3, storeId);

        List<Store> listOfStores = query.getResultList();

        //lista prodavnica prazna
        if (listOfStores.isEmpty()) {
            return -1;
        }

        int newStoreId = -1;

        while (true) {//dok god se ne nadje odgovarajuca prodavnica

            //ispis prodavnica
            System.out.println("Lista prodavnica u kojima se nalazi artikal u dovoljnoj kolicini");
            System.out.println("");
            System.out.println("");
            printAllStores(listOfStores);
            System.out.println("");
            System.out.println("");

            while (true) {
                //unos odgovarajuceg artikla
                System.out.println("Unesite id prodavnice u kojoj zelite da provjerite stanje:");
                newStoreId = scanner.nextInt();
                System.out.println("");
                if (listOfStores.contains(em.find(Store.class, newStoreId)) == false) {
                    System.out.println("Unijeta prodavnica ne postoji u listi");
                    System.out.println("Unesite id ponovo!");
                    System.out.println("");
                    System.out.println("");
                } else {
                    System.out.println("");
                    break;
                }

            }

            //nije htio upit o stanju,svejedno mu je
            if (lastRequestedWrappedState == 0) {
                //vraca se id prodavnice koju je izabrao
                return newStoreId;
            }

            //stanje artikla nije svejedno,salje se upit o stanju menadzeru prodavnice
            //koja je izabrana
            //kako bi se provjerilo da li ima dovoljno
            //artikala na trazenom stanju
            boolean newStoreHasQuantity = checkArticleState(idProduct, quantity, lastRequestedWrappedState, newStoreId);

            if (newStoreHasQuantity == true) {
                //novoizabrana prodavnica ima trazeni broj artikala na stanju
                //vracamo njen id
                return newStoreId;
            }

            //izabrana prodavnica se izbacuje iz liste
            Store removeStore = em.find(Store.class, newStoreId);

            listOfStores.remove(removeStore);

            //lista prodavnica prazna,nema vise izbora,vraca se -1
            if (listOfStores.isEmpty()) {
                return -1;
            }
            System.out.println("");
            System.out.println("Prodavnica koju ste izabrali nema trazenu kolicnu artikala u trazenom stanju");
            System.out.println("Izaberite novu prodavnicu");
            System.out.println("");
        }

    }

    //fja za ispis prodavnica koje imaju artikal na stanju
    //PREPRAVITI DA BUDE FORMATIRAN ISPIS!!!
    private void printAllStores(List<Store> listOfStores) {

        System.out.print("Id prodavnice");
        System.out.print("\t \t");
        System.out.println("Ime prodavnice");

        for (Store store : listOfStores) {
            em.refresh(store);
            System.out.print(store.getIdStore() + "\t");
            System.out.println(store.getName());
        }

    }

    private void createNewBooking(int idProduct, int quantity) {

        int newStore = selectStore(idProduct, quantity);

        //ne postoji prodavnica sa trazenom kolicinom
        if (newStore == -1) {
            //System.out.println("");
            System.out.println("Ne postoji prodavnica sa trazenom kolicinom proizvoda!");
            return;
        }
        System.out.println("");
        System.out.println("Postoji prodavnica sa trazenom kolicinom proizvoda");
        System.out.println("Izaberite opciju");
        System.out.println("1. Pravljenje rezervacije u nadjenoj prodavnici");
        System.out.println("2. Pravljenje rezervacije u ovoj prodavnici");
        System.out.println("");
        int choice = scanner.nextInt();
        System.out.println("");
        while (choice != 1 && choice != 2) {
            System.out.println("Pogresan unos!");
            System.out.println("Unesite ponovo");
            System.out.println("");
            choice = scanner.nextInt();
            System.out.println("");
        }

        //skidanje kolicine proizvoda sa stanja u nadjenoj prodavnici
        //obavezno izvrsiti,njoj se uvijek skida sa stanja
        Onstock onstock = em.find(Onstock.class, new OnstockPK(idProduct, newStore));
        em.refresh(onstock);
        int newQuantity = onstock.getQuantity() - quantity;
        em.getTransaction().begin();
        onstock.setQuantity(newQuantity);
        em.getTransaction().commit();

        //id prodavnice u kojoj se vrsi rezervacija
        int storeForBooking = (choice == 1) ? newStore : storeId;

        contactManagerForBooking(idProduct, quantity, storeForBooking);

    }

    //slanje informacije menadzeru da napravi rezervaciju 
    //idStore je prodavnica cijem menadzeru se obracamo
    private void contactManagerForBooking(int idProduct, int quantity, int idStore) {
        try {
            System.out.println("");
            System.out.println("Unesite kontakt podatke");
            System.out.println("");
            //unos kontakta
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String contact;

            contact = br.readLine();

            TextMessage txt = context.createTextMessage();
            //podaci da prihvati odgovarajuci menadzer
            txt.setIntProperty("idStore", idStore);
            txt.setIntProperty("receiver", 1);

            //ostali podaci
            txt.setIntProperty("function", 2);
            txt.setIntProperty("idProduct", idProduct);
            txt.setIntProperty("quantity", quantity);
            txt.setIntProperty("idVendor", vendorId);
            txt.setStringProperty("contact", contact);

            //responseStore je uvijek storeId prodavca koji se obraca menadzeru
            txt.setIntProperty("responseStore", storeId);
            //slanje  poruke menadzeru
            producer.send(topic, txt);

            String selector = "idProduct = " + idProduct + " AND idStore = " + storeId + " AND idVendor = " + vendorId + "AND function = 2 AND receiver = -1";
            JMSConsumer bookingConsumer = context.createDurableConsumer(topic, "Waiting for booking" + vendorId, selector, false);

            //primanje potvrde od menadzera
            Message msg = bookingConsumer.receive();

            //na kraju ispisati broj dobijene rezervacije!!!da kupac zna koji je id 
            if (msg instanceof TextMessage) {
                TextMessage txtResponse = (TextMessage) msg;

                int idBooking = txtResponse.getIntProperty("idBooking");
                System.out.println("");
                System.out.println("Id vase rezervacije je:" + idBooking);
                System.out.println("");
                bookingConsumer.close();
                return;

            }

            System.out.println("");
            System.out.println("Vasa rezervacija nije uspjela,pokusajte ponovo!");
            System.out.println("");
            bookingConsumer.close();

        } catch (JMSException ex) {
            Logger.getLogger(Vendor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Vendor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
