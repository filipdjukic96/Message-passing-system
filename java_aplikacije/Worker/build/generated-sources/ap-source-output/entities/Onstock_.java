package entities;

import entities.Article;
import entities.OnstockPK;
import entities.Store;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-19T01:22:48")
@StaticMetamodel(Onstock.class)
public class Onstock_ { 

    public static volatile SingularAttribute<Onstock, Integer> quantity;
    public static volatile SingularAttribute<Onstock, OnstockPK> onstockPK;
    public static volatile SingularAttribute<Onstock, Store> store;
    public static volatile SingularAttribute<Onstock, Article> article;

}