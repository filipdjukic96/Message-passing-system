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
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Filip Djukic
 */
@Embeddable
public class TradingPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "idStore")
    private int idStore;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    public TradingPK() {
    }

    public TradingPK(int idStore, Date date) {
        this.idStore = idStore;
        this.date = date;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idStore;
        hash += (date != null ? date.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TradingPK)) {
            return false;
        }
        TradingPK other = (TradingPK) object;
        if (this.idStore != other.idStore) {
            return false;
        }
        if ((this.date == null && other.date != null) || (this.date != null && !this.date.equals(other.date))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TradingPK[ idStore=" + idStore + ", date=" + date + " ]";
    }
    
}
