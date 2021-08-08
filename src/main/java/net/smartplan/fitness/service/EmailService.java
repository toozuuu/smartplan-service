package net.smartplan.fitness.service;

import net.smartplan.fitness.dto.EmailBodyDTO;

import java.util.List;

public interface EmailService {

    void sendEmail(String mail, String name, double price, double quantity, String add);

    void sendEmailWithTemplate(List<EmailBodyDTO> list, String recipient, double total, String address);
}
