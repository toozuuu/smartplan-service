package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.PurchaseDTO;

import java.util.List;

public interface PurchaseService {

    PurchaseDTO save(PurchaseDTO purchaseDTO);

    List<PurchaseDTO> getAllByUser(String email);
}
