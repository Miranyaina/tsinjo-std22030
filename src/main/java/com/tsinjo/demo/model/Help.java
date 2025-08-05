package com.tsinjo.demo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "help")
public class Help {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_id")
    private Payment payment;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String accidentDescription;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Help(Long id, Beneficiary beneficiary, String accidentDescription) {
        this.id = id;
        this.beneficiary = beneficiary;
        this.accidentDescription = accidentDescription;
    }
}
