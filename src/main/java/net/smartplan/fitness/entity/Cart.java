package net.smartplan.fitness.entity;

import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "cart")
@XmlRootElement
@Data
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
}
