package com.vamsi.MoneyManagerApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    public String fromEmail;


    public void sendEmail(String to,String subject,String body){
            try{
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(fromEmail);
                message.setSubject(subject);
                message.setTo(to);
                message.setText(body);
                javaMailSender.send(message);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
    }
}
