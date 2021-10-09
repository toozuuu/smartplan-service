
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
@Table(name = "meal_ingredients")
@XmlRootElement
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class MealIngredients implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ingredients_name")
    private String ingredientsName;
    @Column(name = "unit")
    private String unit;
    @Column(name = "protein")
    private Double protein;
    @Column(name = "carbs")
    private Double carbs;
    @Column(name = "fat")
    private Double fat;
    @Column(name = "quantity")
    private Double quantity;
    @JoinColumn(name = "meal_id", referencedColumnName = "id")
    @ManyToOne
    private Meal mealId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MealIngredients that = (MealIngredients) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
