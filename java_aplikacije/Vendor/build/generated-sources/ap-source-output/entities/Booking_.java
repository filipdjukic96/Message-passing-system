package entities;

import entities.Article;
import entities.Store;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-19T23:31:20")
@StaticMetamodel(Booking.class)
public class Booking_ { 

    public static volatile SingularAttribute<Booking, Date> date;
    public static volatile SingularAttribute<Booking, Integer> quantity;
    public static volatile SingularAttribute<Booking, Article> idArticle;
    public static volatile SingularAttribute<Booking, Integer> idBooking;
    public static volatile SingularAttribute<Booking, String> contact;
    public static volatile SingularAttribute<Booking, Store> idStore;

}