
package net.smartplan.fitness.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "purchase")
@XmlRootElement
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Purchase implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "purchase_id")
    private String purchaseId;
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
    @ToString.Exclude
    private Collection<PurchaseDetails> purchaseDetailsCollection;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Purchase purchase = (Purchase) o;
        return Objects.equals(purchaseId, purchase.purchaseId);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
