package com.tsinjo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tsinjo.demo.model.Payment;
import com.tsinjo.demo.model.PaymentStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByStatus(PaymentStatus status);
}
