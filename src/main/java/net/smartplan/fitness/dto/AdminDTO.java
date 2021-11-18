package net.smartplan.fitness.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AdminDTO {

    private String email;
    private String password;
    private boolean isLogged = false;
    private Date created;
    private Date updated;
}
