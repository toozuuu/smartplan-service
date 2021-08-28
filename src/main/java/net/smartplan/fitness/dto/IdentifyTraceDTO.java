package net.smartplan.fitness.dto;

import lombok.Data;

import java.util.Date;

@Data
public class IdentifyTraceDTO {

    private Integer id;
    private String email;
    private Double goalDays;
    private Date goalExpiredDate;
    private String status = "ACTIVE";
    private boolean dailyStatus = false;
    private Date created;
    private Date updated;

}
