package com.tsinjo.demo.model;

import java.time.LocalDateTime;

public class Donation {
    private long id;
    private Donor donor;
    private Payment payment;
    private LocalDateTime createdAt;

    public Donation(long id, Donor donor, Payment payment, LocalDateTime createdAt) {
        this.id = id;
        this.donor = donor;
        this.payment = payment;
        this.createdAt = createdAt;
    }
}
