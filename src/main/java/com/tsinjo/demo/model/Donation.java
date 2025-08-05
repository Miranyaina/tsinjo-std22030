package com.tsinjo.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "donation")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "donor_id")
    private Donor donor;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Donation(Donor donor, Payment payment, LocalDateTime createdAt) {
        this.donor = donor;
        this.payment = payment;
        this.createdAt = LocalDateTime.now();
    }
}
