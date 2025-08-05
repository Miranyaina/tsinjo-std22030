package com.tsinjo.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.tsinjo.demo.model.Payment;
import com.tsinjo.demo.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class VolaService {
    private static final Logger log = LoggerFactory.getLogger(VolaService.class);
    private final RestTemplate restTemplate;

    @Value("${vola.api.url:https://42cwka3n4ifcp7ufheyrpmph240iuaxo.lambda-url.eu-west-3.on.aws}")
    private String volaApiUrl;

    @Value("${vola.api.key}")
    private String apiKey;

    public VolaService() {
        this.restTemplate = new RestTemplate();
    }

    public Payment verifyPayment(String paymentId) {
        try {
            log.info("Verifying payment with ID: {}", paymentId);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-API-Key", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    volaApiUrl + "/payments/" + paymentId,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> paymentData = response.getBody();
                return mapToPayment(paymentData);
            } else {
                log.warn("Failed to verify payment {}: {}", paymentId, response.getStatusCode());
                return createFailedPayment(paymentId);
            }
        } catch (Exception e) {
            log.error("Error verifying payment {}: {}", paymentId, e.getMessage());
            return createFailedPayment(paymentId);
        }
    }

    private Payment mapToPayment(Map<String, Object> paymentData) {
        String id = (String) paymentData.get("id");
        String status = (String) paymentData.get("status");
        Object amountObj = paymentData.get("amount");
        String paymentMethod = (String) paymentData.get("payment_method");
        String dateStr = (String) paymentData.get("payment_date");

        Payment payment = new Payment();
        payment.setId(id);
        payment.setStatus(PaymentStatus.valueOf(status.toUpperCase()));
        payment.setAmount(new BigDecimal(amountObj.toString()));
        payment.setPaymentMethod(paymentMethod != null ? paymentMethod : "UNKNOWN");
        payment.setPaymentDate(dateStr != null ? LocalDateTime.parse(dateStr) : LocalDateTime.now());

        return payment;
    }

    private Payment createFailedPayment(String paymentId) {
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus(PaymentStatus.FAILED);
        payment.setAmount(BigDecimal.ZERO);
        payment.setPaymentMethod("UNKNOWN");
        payment.setPaymentDate(LocalDateTime.now());
        return payment;
    }
}
