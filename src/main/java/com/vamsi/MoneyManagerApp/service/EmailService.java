package com.vamsi.MoneyManagerApp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Value("${BREVO_API_KEY}")
    private String brevoApiKey;

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    public void sendEmail(String to, String subject, String htmlContent) {
        try {
            log.info("📤 Sending email via Brevo API to {}", to);

            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key", brevoApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            String body = String.format("""
                {
                  "sender": {"email": "%s"},
                  "to": [{"email": "%s"}],
                  "subject": "%s",
                  "htmlContent": "%s"
                }
            """, fromEmail, to, subject, htmlContent.replace("\"", "'"));

            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(BREVO_API_URL, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("✅ Email sent successfully to {}", to);
            } else {
                log.error("❌ Failed to send email: {}", response.getBody());
                throw new RuntimeException("Email sending failed: " + response.getBody());
            }

        } catch (Exception e) {
            log.error("❌ Email sending failed: {}", e.getMessage());
            throw new RuntimeException("Email sending failed: " + e.getMessage(), e);
        }
    }
}
