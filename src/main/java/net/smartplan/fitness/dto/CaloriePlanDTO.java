package net.smartplan.fitness.dto;

import lombok.Data;

@Data
public class CaloriePlanDTO {


    private Integer id;
    private Double protein;
    private Double carbs;
    private Double fat;
    private Double mealsPerDay;
    private String type;
}
