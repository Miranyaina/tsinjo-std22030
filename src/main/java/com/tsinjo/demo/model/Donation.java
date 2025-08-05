package com.tsinjo.demo.model;

import java.time.LocalDateTime;

public class Donation {
    private long id;
    private Donor donor;
    private Payment payment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Donation(long id, Donor donor, Payment payment, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.donor = donor;
        this.payment = payment;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
