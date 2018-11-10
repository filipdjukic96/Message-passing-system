package entities;

import entities.Booking;
import entities.Onstock;
import entities.Trading;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-19T01:13:35")
@StaticMetamodel(Store.class)
public class Store_ { 

    public static volatile ListAttribute<Store, Trading> tradingList;
    public static volatile ListAttribute<Store, Booking> bookingList;
    public static volatile SingularAttribute<Store, String> name;
    public static volatile SingularAttribute<Store, Integer> idStore;
    public static volatile ListAttribute<Store, Onstock> onstockList;

}