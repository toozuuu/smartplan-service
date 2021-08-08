package net.smartplan.fitness.dto;

import lombok.Data;

@Data
public class PurchaseDetailsDTO {

    private Integer id;
    private Double price;
    private Double quantity;
    private String comment;
    private String orderType;
    private String orderDate;
    private String orderTime;
    private String shippingAddress;
    private MealDTO mealId;
}
