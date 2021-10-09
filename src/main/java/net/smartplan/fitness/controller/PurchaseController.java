package net.smartplan.fitness.controller;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.PurchaseDTO;
import net.smartplan.fitness.dto.UpdatedPurchaseDetailsDTO;
import net.smartplan.fitness.response.CommonResponse;
import net.smartplan.fitness.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * @author H.D. Sachin Dilshan
 */

@RestController
@RequestMapping("/purchase")
@CrossOrigin
@Slf4j
public class PurchaseController {

    private final PurchaseService purchaseService;


    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;

    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse> save(@RequestBody PurchaseDTO purchaseDTO) {

        try {
            purchaseService.save(purchaseDTO);
            return new ResponseEntity<>(new CommonResponse(true, "Success"), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(new CommonResponse(false, e.toString()), HttpStatus.OK);
        }


    }

    @GetMapping("/getOrdersByUser/{email}")
    public List<PurchaseDTO> getAllByUser(@PathVariable String email) {
        return purchaseService.getAllByUser(email);
    }

    @GetMapping("/getOrders")
    public List<PurchaseDTO> getAllOrders() {
        return purchaseService.fetchAllOrders();
    }


    @PostMapping("/updateOrderStatus")
    public ResponseEntity<CommonResponse> updateOrderStatus(@RequestBody UpdatedPurchaseDetailsDTO updatedPurchaseDetailsDTO) {

        try {
            purchaseService.updateOrderStatus(updatedPurchaseDetailsDTO);
            return new ResponseEntity<>(new CommonResponse(true, "Success"), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(new CommonResponse(false, e.toString()), HttpStatus.OK);
        }
    }


    @GetMapping("/generateExcel/report")
    public ResponseEntity<InputStreamResource> excelGenerator() {

        ByteArrayInputStream in = purchaseService.getOrdersReport();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=meals report.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }
}
