package com.tsinjo.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.tsinjo.demo.model.Payment;
import com.tsinjo.demo.model.PaymentStatus;
import com.tsinjo.demo.repository.PaymentRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentVerificationService {
    private static final Logger log = LoggerFactory.getLogger(PaymentVerificationService.class);

    private final PaymentRepository paymentRepository;
    private final VolaService volaService;

    public PaymentVerificationService(PaymentRepository paymentRepository, VolaService volaService) {
        this.paymentRepository = paymentRepository;
        this.volaService = volaService;
    }

    @Async
    public CompletableFuture<Payment> verifyPaymentAsync(String paymentId) {
        log.info("Starting async verification for payment: {}", paymentId);
        Payment verifiedPayment = volaService.verifyPayment(paymentId);
        paymentRepository.save(verifiedPayment);
        log.info("Payment {} verification completed with status: {}", paymentId, verifiedPayment.getStatus());
        return CompletableFuture.completedFuture(verifiedPayment);
    }

    @Scheduled(fixedRate = 30000) // Vérifie toutes les 30 secondes
    public void checkPendingPayments() {
        List<Payment> pendingPayments = paymentRepository.findByStatus(PaymentStatus.VERIFYING);
        if (!pendingPayments.isEmpty()) {
            log.info("Checking {} pending payments", pendingPayments.size());

            for (Payment payment : pendingPayments) {
                try {
                    Payment updated = volaService.verifyPayment(payment.getId());
                    if (updated.getStatus() != PaymentStatus.VERIFYING) {
                        paymentRepository.save(updated);
                        log.info("Payment {} status updated to: {}", payment.getId(), updated.getStatus());
                    }
                } catch (Exception e) {
                    log.error("Error checking payment {}: {}", payment.getId(), e.getMessage());
                }
            }
        }
    }
}