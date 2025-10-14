package com.vamsi.MoneyManagerApp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Value("${BREVO_API_KEY}")
    private String brevoApiKey; // empty locally, set in Render

    @Value("${spring.profiles.active:local}")
    private String activeProfile; // default 'local'

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    public void sendEmail(String to, String subject, String body) {
        try {
            if (isRenderEnvironment()) {
                sendUsingBrevoApi(to, subject, body);
            } else {
                sendUsingSMTP(to, subject, body);
            }
        } catch (Exception e) {
            throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
        }
    }

    private void sendUsingSMTP(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    private void sendUsingBrevoApi(String to, String subject, String body) {
        if (brevoApiKey == null || brevoApiKey.isEmpty()) {
            throw new RuntimeException("BREVO_API_KEY not set for Render environment");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", brevoApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String jsonBody = String.format("""
            {
              "sender": {"email": "%s"},
              "to": [{"email": "%s"}],
              "subject": "%s",
              "htmlContent": "%s"
            }
        """, fromEmail, to, subject, body.replace("\"", "'"));

        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
        restTemplate.postForEntity(BREVO_API_URL, entity, String.class);
    }

    private boolean isRenderEnvironment() {
        // Render automatically sets RENDER environment variable
        return System.getenv("RENDER") != null || !"local".equalsIgnoreCase(activeProfile);
    }
}
