package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.PurchaseDTO;
import net.smartplan.fitness.dto.PurchaseDetailsDTO;
import net.smartplan.fitness.dto.UpdatedPurchaseDetailsDTO;

import java.util.List;


/**
 * @author H.D. Sachin Dilshan
 */


public interface PurchaseService {

    PurchaseDTO save(PurchaseDTO purchaseDTO);

    List<PurchaseDTO> getAllByUser(String email);

    List<PurchaseDTO> fetchAllOrders();

    PurchaseDetailsDTO updateOrderStatus(UpdatedPurchaseDetailsDTO updatedPurchaseDetailsDTO);
}
