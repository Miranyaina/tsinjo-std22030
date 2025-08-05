package com.tsinjo.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tsinjo.demo.model.*;
import com.tsinjo.demo.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TsinjoService {
    private static final Logger log = LoggerFactory.getLogger(TsinjoService.class);

    private final DonationRepository donationRepository;
    private final HelpRepository helpRepository;
    private final DonorRepository donorRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentVerificationService paymentVerificationService;

    public TsinjoService(DonationRepository donationRepository,
                         HelpRepository helpRepository,
                         DonorRepository donorRepository,
                         PaymentRepository paymentRepository,
                         PaymentVerificationService paymentVerificationService) {
        this.donationRepository = donationRepository;
        this.helpRepository = helpRepository;
        this.donorRepository = donorRepository;
        this.paymentRepository = paymentRepository;
        this.paymentVerificationService = paymentVerificationService;
    }

    public Donation createDonation(String donorEmail, String donorName, String paymentId,
                                   BigDecimal amount, String paymentMethod) {
        log.info("Creating donation for donor: {} with payment: {}", donorEmail, paymentId);

        // Créer ou récupérer le donateur
        Donor donor = donorRepository.findByEmail(donorEmail)
                .orElse(new Donor(donorEmail, donorName));

        // Créer le paiement avec statut VERIFYING
        Payment payment = new Payment(paymentId, LocalDateTime.now(), amount, paymentMethod);

        // Créer la donation
        Donation donation = new Donation(donor, payment);

        // Sauvegarder
        Donation savedDonation = donationRepository.save(donation);

        // Démarrer la vérification asynchrone
        paymentVerificationService.verifyPaymentAsync(paymentId);

        log.info("Donation created with ID: {}", savedDonation.getId());
        return savedDonation;
    }

    public List<Object> getAllTransactions() {
        List<Object> transactions = new ArrayList<>();

        // Récupérer les donations réussies
        List<Donation> donations = donationRepository.findAllSuccessfulOrderByCreatedAtDesc();
        transactions.addAll(donations);

        // Récupérer les aides réussies
        List<Help> helps = helpRepository.findAllSuccessfulOrderByCreatedAtDesc();
        transactions.addAll(helps);

        // Trier par date de création (plus récent en premier)
        transactions.sort((a, b) -> {
            LocalDateTime dateA = a instanceof Donation ?
                    ((Donation) a).getCreatedAt() : ((Help) a).getCreatedAt();
            LocalDateTime dateB = b instanceof Donation ?
                    ((Donation) b).getCreatedAt() : ((Help) b).getCreatedAt();
            return dateB.compareTo(dateA);
        });

        return transactions;
    }

    public Optional<Payment> getPaymentStatus(String paymentId) {
        return paymentRepository.findById(paymentId);
    }
}