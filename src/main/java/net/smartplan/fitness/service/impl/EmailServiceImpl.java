package net.smartplan.fitness.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.EmailBodyDTO;
import net.smartplan.fitness.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final String emailFrom;
    private final TemplateEngine templateEngine;


    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender,
                            @Value("${spring.mail.username}") String emailFrom,
                            TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.emailFrom = emailFrom;
        this.templateEngine = templateEngine;
    }


    @Override
    public void sendEmail(String mail, String name, double price, double quantity, String add) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(mail);

        msg.setSubject("Order Confirmed");
        msg.setText("Your order has been confirmed...!\n"+
                "Total Price : " + price + "\n" +
                "Address : " + add);

        javaMailSender.send(msg);
    }
    @Override
    public void sendEmailWithTemplate(List<EmailBodyDTO> list, String recipient, double total, String address) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            String content = setTemplateContent(list,Double.toString(total), address);
            messageHelper.setFrom(emailFrom);
            messageHelper.setTo(recipient);
            messageHelper.setCc("kumarajith.illukkumbura@gmail.com");
            messageHelper.setSubject("K-Plan Product Purchase");
            messageHelper.setText(content,true);
        };
        try {
            javaMailSender.send(mimeMessagePreparator);
        } catch (MailException e) {
            log.error("Error with sending mail... "+ e);
        }
    }

    public String setTemplateContent(List<EmailBodyDTO> list, String total, String address) {
        Context context = new Context();
        context.setVariable("products", list);
        context.setVariable("total_amount","RS .".concat(total));
        context.setVariable("address",address);
        return templateEngine.process("confirmOrder", context);
    }
}
