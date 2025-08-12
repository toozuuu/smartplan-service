package net.smartplan.fitness.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.smartplan.fitness.dto.EmailBodyDTO;
import net.smartplan.fitness.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.transaction.Transactional;
import java.util.Date;
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
    public void sendEmailWithTemplate(List<EmailBodyDTO> list, String recipient, double total, String address,String orderId) {
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            String content = setTemplateContent(list,Double.toString(total), address,orderId,recipient);
            messageHelper.setFrom(emailFrom);
            messageHelper.setTo(recipient);
            messageHelper.setCc("sachindilshan040@gmail.com");
            messageHelper.setSubject("Smart Plan Meal Purchase");
            messageHelper.setText(content,true);
        };
        try {
            javaMailSender.send(mimeMessagePreparator);
        } catch (MailException e) {
            log.error("Error with sending mail... "+ e);
        }
    }

    public String setTemplateContent(List<EmailBodyDTO> list, String total, String address,String orderId,String recipient) {
        Context context = new Context();
        context.setVariable("product_name", list.get(0).getName());
        context.setVariable("order_id", orderId);
        context.setVariable("recipient", recipient +',');
        context.setVariable("order_date",new Date());
        context.setVariable("total_amount","$ .".concat(total));
        context.setVariable("address",address);
        return templateEngine.process("confirmOrder", context);
    }
}
