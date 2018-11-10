/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Filip Djukic
 */
@Embeddable
public class OnstockPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "idArticle")
    private int idArticle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "idStore")
    private int idStore;

    public OnstockPK() {
    }

    public OnstockPK(int idArticle, int idStore) {
        this.idArticle = idArticle;
        this.idStore = idStore;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idArticle;
        hash += (int) idStore;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OnstockPK)) {
            return false;
        }
        OnstockPK other = (OnstockPK) object;
        if (this.idArticle != other.idArticle) {
            return false;
        }
        if (this.idStore != other.idStore) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.OnstockPK[ idArticle=" + idArticle + ", idStore=" + idStore + " ]";
    }
    
}
