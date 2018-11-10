/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "trading")
@NamedQueries({
    @NamedQuery(name = "Trading.findAll", query = "SELECT t FROM Trading t"),
    @NamedQuery(name = "Trading.findByIdStore", query = "SELECT t FROM Trading t WHERE t.tradingPK.idStore = :idStore"),
    @NamedQuery(name = "Trading.findByDate", query = "SELECT t FROM Trading t WHERE t.tradingPK.date = :date"),
    @NamedQuery(name = "Trading.findByQuantity", query = "SELECT t FROM Trading t WHERE t.quantity = :quantity"),
    @NamedQuery(name = "Trading.findByTotalSum", query = "SELECT t FROM Trading t WHERE t.totalSum = :totalSum")})
public class Trading implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TradingPK tradingPK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @NotNull
    @Column(name = "totalSum")
    private double totalSum;
    @JoinColumn(name = "idStore", referencedColumnName = "idStore", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Store store;

    public Trading() {
    }

    public Trading(TradingPK tradingPK) {
        this.tradingPK = tradingPK;
    }

    public Trading(TradingPK tradingPK, int quantity, double totalSum) {
        this.tradingPK = tradingPK;
        this.quantity = quantity;
        this.totalSum = totalSum;
    }

    public Trading(int idStore, Date date) {
        this.tradingPK = new TradingPK(idStore, date);
    }

    public TradingPK getTradingPK() {
        return tradingPK;
    }

    public void setTradingPK(TradingPK tradingPK) {
        this.tradingPK = tradingPK;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(double totalSum) {
        this.totalSum = totalSum;
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
        hash += (tradingPK != null ? tradingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trading)) {
            return false;
        }
        Trading other = (Trading) object;
        if ((this.tradingPK == null && other.tradingPK != null) || (this.tradingPK != null && !this.tradingPK.equals(other.tradingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Trading[ tradingPK=" + tradingPK + " ]";
    }
    
}
