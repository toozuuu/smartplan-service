package net.smartplan.fitness.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UserDTO {


    private Integer id;
    private String name;
    private String gender;
    private String consulter;
    private String email;
    private String password;
    private Double age;
    private Double weight;
    private String goalType;
    private Double goalTime;
    private Double height;
    private Double bodyFat;
    private Double fatFreeMass;
    private Double estimatedBmr;
    private String activityLevel;
    private Double caloriesToAdd;
    private Double dailyReq;
    private Double dailyReqNon;
    private List<MacronutrientFoodDTO> macronutrientFoodList = new ArrayList<>();
    private List<CaloriePlanDTO> caloriePlanList = new ArrayList<>();
    private AddressDTO address;

    private boolean isSuccess;
    private Date created;
    private long numOfDays;
}
