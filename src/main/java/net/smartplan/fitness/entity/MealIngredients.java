/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author H.D. Sachin Dilshan
 */
@Entity
@Table(name = "meal_ingredients")
@XmlRootElement
@Data
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

}
