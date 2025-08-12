
package net.smartplan.fitness.entity;

import lombok.*;
import org.hibernate.Hibernate;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author H.D. Sachin Dilshan
 */

@Entity
@Table(name = "calorie_plan")
@XmlRootElement
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CaloriePlan implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "protein")
    private Double protein;
    @Column(name = "carbs")
    private Double carbs;
    @Column(name = "fat")
    private Double fat;
    @Column(name = "meals_per_day")
    private Double mealsPerDay;
    @Column(name = "type")
    private String type;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CaloriePlan that = (CaloriePlan) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
