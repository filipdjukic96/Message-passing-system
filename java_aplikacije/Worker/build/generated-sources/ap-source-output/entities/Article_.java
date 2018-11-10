package entities;

import entities.Booking;
import entities.Onstock;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-01-19T01:22:48")
@StaticMetamodel(Article.class)
public class Article_ { 

    public static volatile SingularAttribute<Article, Integer> idArticle;
    public static volatile SingularAttribute<Article, Double> price;
    public static volatile ListAttribute<Article, Booking> bookingList;
    public static volatile SingularAttribute<Article, String> name;
    public static volatile SingularAttribute<Article, String> type;
    public static volatile ListAttribute<Article, Onstock> onstockList;

}