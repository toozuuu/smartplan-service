package net.smartplan.fitness.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PurchaseDTO {

    private String purchaseId;
    private String email;
    private Date created;
    private Date updated;
    private List<PurchaseDetailsDTO> purchaseDetails;
}
