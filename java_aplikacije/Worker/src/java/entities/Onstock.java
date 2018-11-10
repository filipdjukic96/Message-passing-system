/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Filip Djukic
 */
@Entity
@Table(name = "onstock")
@NamedQueries({
    @NamedQuery(name = "Onstock.findAll", query = "SELECT o FROM Onstock o"),
    @NamedQuery(name = "Onstock.findByQuantity", query = "SELECT o FROM Onstock o WHERE o.quantity = :quantity"),
    @NamedQuery(name = "Onstock.findByIdArticle", query = "SELECT o FROM Onstock o WHERE o.onstockPK.idArticle = :idArticle"),
    @NamedQuery(name = "Onstock.findByIdStore", query = "SELECT o FROM Onstock o WHERE o.onstockPK.idStore = :idStore")})
public class Onstock implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected OnstockPK onstockPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @JoinColumn(name = "idArticle", referencedColumnName = "idArticle", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Article article;
    @JoinColumn(name = "idStore", referencedColumnName = "idStore", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Store store;

    public Onstock() {
    }

    public Onstock(OnstockPK onstockPK) {
        this.onstockPK = onstockPK;
    }

    public Onstock(OnstockPK onstockPK, int quantity) {
        this.onstockPK = onstockPK;
        this.quantity = quantity;
    }

    public Onstock(int idArticle, int idStore) {
        this.onstockPK = new OnstockPK(idArticle, idStore);
    }

    public OnstockPK getOnstockPK() {
        return onstockPK;
    }

    public void setOnstockPK(OnstockPK onstockPK) {
        this.onstockPK = onstockPK;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (onstockPK != null ? onstockPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Onstock)) {
            return false;
        }
        Onstock other = (Onstock) object;
        if ((this.onstockPK == null && other.onstockPK != null) || (this.onstockPK != null && !this.onstockPK.equals(other.onstockPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Onstock[ onstockPK=" + onstockPK + " ]";
    }
    
}
