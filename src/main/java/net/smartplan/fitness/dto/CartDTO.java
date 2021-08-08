package net.smartplan.fitness.dto;

import lombok.Data;

@Data
public class CartDTO {

    private Integer id;
    private String email;
    private Double unitPrice;
    private Double quantity;
    private MealDTO mealId;
}
