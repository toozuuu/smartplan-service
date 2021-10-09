package net.smartplan.fitness.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MealDTO {

    private Integer id;
    private String mealName;
    private String mealType;
    private Double price;
    private String image;
    private String status = "ACTIVE";
    private List<MealIngredientsDTO> mealIngredientsCollection;
    private Date created;
    private Date updated;

}
