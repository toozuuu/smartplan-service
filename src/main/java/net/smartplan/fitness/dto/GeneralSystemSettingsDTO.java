package net.smartplan.fitness.dto;

import lombok.Data;

import java.util.Date;

@Data
public class GeneralSystemSettingsDTO {

    private String code;
    private String value;
    private Date updated;

}
