package net.smartplan.fitness.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UnitTypeDTO {

    private Integer id;
    private String description;
    private Date created;
    private Date updated;

}
