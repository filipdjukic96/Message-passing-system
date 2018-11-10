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
@Table(name = "store")
@NamedQueries({
    @NamedQuery(name = "Store.findAll", query = "SELECT s FROM Store s"),
    @NamedQuery(name = "Store.findByIdStore", query = "SELECT s FROM Store s WHERE s.idStore = :idStore"),
    @NamedQuery(name = "Store.findByName", query = "SELECT s FROM Store s WHERE s.name = :name")})
public class Store implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idStore")
    private Integer idStore;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
    private List<Trading> tradingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idStore")
    private List<Booking> bookingList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "store")
    private List<Onstock> onstockList;

    public Store() {
    }

    public Store(Integer idStore) {
        this.idStore = idStore;
    }

    public Store(Integer idStore, String name) {
        this.idStore = idStore;
        this.name = name;
    }

    public Integer getIdStore() {
        return idStore;
    }

    public void setIdStore(Integer idStore) {
        this.idStore = idStore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Trading> getTradingList() {
        return tradingList;
    }

    public void setTradingList(List<Trading> tradingList) {
        this.tradingList = tradingList;
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
        hash += (idStore != null ? idStore.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Store)) {
            return false;
        }
        Store other = (Store) object;
        if ((this.idStore == null && other.idStore != null) || (this.idStore != null && !this.idStore.equals(other.idStore))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Store[ idStore=" + idStore + " ]";
    }
    
}
