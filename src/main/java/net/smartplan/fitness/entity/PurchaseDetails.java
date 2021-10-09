
package net.smartplan.fitness.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "purchase_details")
@XmlRootElement
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PurchaseDetails that = (PurchaseDetails) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
