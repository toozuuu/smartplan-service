package net.smartplan.fitness.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "cart")
@XmlRootElement
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Cart {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "user_name")
    private String email;
    @Column(name = "unit_price")
    private Double unitPrice;
    @Column(name = "qty")
    private Double quantity;
    @JoinColumn(name = "meal_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Meal mealId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
