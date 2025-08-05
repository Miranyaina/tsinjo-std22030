package com.tsinjo.demo.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.tsinjo.demo.model.Donation;
import com.tsinjo.demo.service.TsinjoService;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class TsinjoController {
    private static final Logger log = LoggerFactory.getLogger(TsinjoController.class);

    private final TsinjoService tsinjoService;

    public TsinjoController(TsinjoService tsinjoService) {
        this.tsinjoService = tsinjoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        log.info("Loading Tsinjo homepage");

        List<Object> transactions = tsinjoService.getAllTransactions();
        model.addAttribute("transactions", transactions);

        return "index";
    }

    @PostMapping("/donations")
    public String createDonation(@RequestParam String donorEmail,
                                 @RequestParam String donorName,
                                 @RequestParam String paymentId,
                                 @RequestParam BigDecimal amount,
                                 @RequestParam String paymentMethod,
                                 RedirectAttributes redirectAttributes) {
        try {
            log.info("Creating donation - Email: {}, Name: {}, PaymentId: {}, Amount: {}",
                    donorEmail, donorName, paymentId, amount);

            Donation donation = tsinjoService.createDonation(donorEmail, donorName, paymentId, amount, paymentMethod);

            redirectAttributes.addFlashAttribute("successMessage",
                    "Don soumis avec succès ! ID: " + donation.getId() + ". Vérification en cours...");

        } catch (Exception e) {
            log.error("Error creating donation: {}", e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Erreur lors de la soumission du don: " + e.getMessage());
        }

        return "redirect:/";
    }
}
