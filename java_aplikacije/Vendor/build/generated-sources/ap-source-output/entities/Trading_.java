package entities;

import entities.Store;
import entities.TradingPK;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-19T23:31:20")
@StaticMetamodel(Trading.class)
public class Trading_ { 

    public static volatile SingularAttribute<Trading, Double> totalSum;
    public static volatile SingularAttribute<Trading, Integer> quantity;
    public static volatile SingularAttribute<Trading, TradingPK> tradingPK;
    public static volatile SingularAttribute<Trading, Store> store;

}