
package net.smartplan.fitness.entity;

import lombok.Data;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author H.D. Sachin Dilshan
 */
@Entity
@Table(name = "purchase_details")
@XmlRootElement
@Data
public class PurchaseDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "price")
    private Double price;
    @Column(name = "comment")
    private String comment;
    @Column(name = "orderType")
    private String orderType;
    @Column(name = "orderDate")
    private String orderDate;
    @Column(name = "orderTime")
    private String orderTime;
    @Column(name = "quantity")
    private Double quantity;
    @Column(name = "status")
    private String status;
    @Lob
    @Column(name = "shipping_address")
    private String shippingAddress;
    @JoinColumn(name = "meal_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Meal mealId;
    @JoinColumn(name = "purchase_id", referencedColumnName = "purchase_id")
    @ManyToOne(optional = false)
    private Purchase purchaseId;

    
}
