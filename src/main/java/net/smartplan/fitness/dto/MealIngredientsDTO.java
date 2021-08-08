package net.smartplan.fitness.dto;

import lombok.Data;

@Data
public class MealIngredientsDTO {

    private Integer id;
    private String ingredientsName;
    private String unit;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double quantity;
}
