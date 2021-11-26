package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.EmailBodyDTO;

import java.util.List;


/**
 * @author H.D. Sachin Dilshan
 */


public interface EmailService {

    void sendEmailWithTemplate(List<EmailBodyDTO> list, String recipient, double total, String address,String orderId);
}
