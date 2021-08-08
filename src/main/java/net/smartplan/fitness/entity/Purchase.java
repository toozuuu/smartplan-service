/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.smartplan.fitness.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Sahan
 */
@Entity
@Table(name = "purchase")
@XmlRootElement
@Data
public class Purchase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "purchase_id")
    private Integer purchaseId;
    @Column(name = "user_name")
    private String email;
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "purchaseId")
    private Collection<PurchaseDetails> purchaseDetailsCollection;

    
}
