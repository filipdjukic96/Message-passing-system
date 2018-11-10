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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Filip Djukic
 */
@Entity
@Table(name = "booking")
@NamedQueries({
    @NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b"),
    @NamedQuery(name = "Booking.findByIdBooking", query = "SELECT b FROM Booking b WHERE b.idBooking = :idBooking"),
    @NamedQuery(name = "Booking.findByQuantity", query = "SELECT b FROM Booking b WHERE b.quantity = :quantity"),
    @NamedQuery(name = "Booking.findByContact", query = "SELECT b FROM Booking b WHERE b.contact = :contact"),
    @NamedQuery(name = "Booking.findByDate", query = "SELECT b FROM Booking b WHERE b.date = :date")})
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idBooking")
    private Integer idBooking;
    @Basic(optional = false)
    @NotNull
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "contact")
    private String contact;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @JoinColumn(name = "idArticle", referencedColumnName = "idArticle")
    @ManyToOne(optional = false)
    private Article idArticle;
    @JoinColumn(name = "idStore", referencedColumnName = "idStore")
    @ManyToOne(optional = false)
    private Store idStore;

    public Booking() {
    }

    public Booking(Integer idBooking) {
        this.idBooking = idBooking;
    }

    public Booking(Integer idBooking, int quantity, String contact, Date date) {
        this.idBooking = idBooking;
        this.quantity = quantity;
        this.contact = contact;
        this.date = date;
    }

    public Booking(int quantity, String contact, Date date) {

        this.quantity = quantity;
        this.contact = contact;
        this.date = date;
    }

    public Integer getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(Integer idBooking) {
        this.idBooking = idBooking;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Article getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Article idArticle) {
        this.idArticle = idArticle;
    }

    public Store getIdStore() {
        return idStore;
    }

    public void setIdStore(Store idStore) {
        this.idStore = idStore;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBooking != null ? idBooking.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Booking)) {
            return false;
        }
        Booking other = (Booking) object;
        if ((this.idBooking == null && other.idBooking != null) || (this.idBooking != null && !this.idBooking.equals(other.idBooking))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Booking[ idBooking=" + idBooking + " ]";
    }

}
