/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Filip Djukic
 */
@Entity
@Table(name = "article")
@NamedQueries({
    @NamedQuery(name = "Article.findAll", query = "SELECT a FROM Article a"),
    @NamedQuery(name = "Article.findByIdArticle", query = "SELECT a FROM Article a WHERE a.idArticle = :idArticle"),
    @NamedQuery(name = "Article.findByName", query = "SELECT a FROM Article a WHERE a.name = :name"),
    @NamedQuery(name = "Article.findByType", query = "SELECT a FROM Article a WHERE a.type = :type"),
    @NamedQuery(name = "Article.findByPrice", query = "SELECT a FROM Article a WHERE a.price = :price")})
public class Article implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idArticle")
    private Integer idArticle;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private double price;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idArticle")
    private List<Booking> bookingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "article")
    private List<Onstock> onstockList;

    public Article() {
    }

    public Article(Integer idArticle) {
        this.idArticle = idArticle;
    }

    public Article(Integer idArticle, String name, String type, double price) {
        this.idArticle = idArticle;
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public Integer getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Integer idArticle) {
        this.idArticle = idArticle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public List<Onstock> getOnstockList() {
        return onstockList;
    }

    public void setOnstockList(List<Onstock> onstockList) {
        this.onstockList = onstockList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArticle != null ? idArticle.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Article)) {
            return false;
        }
        Article other = (Article) object;
        if ((this.idArticle == null && other.idArticle != null) || (this.idArticle != null && !this.idArticle.equals(other.idArticle))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Article[ idArticle=" + idArticle + " ]";
    }
    
}
