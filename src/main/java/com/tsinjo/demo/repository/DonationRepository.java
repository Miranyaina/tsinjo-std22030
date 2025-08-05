package com.tsinjo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.tsinjo.demo.model.Donation;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {
    @Query("SELECT d FROM Donation d JOIN d.payment p WHERE p.status = 'SUCCEEDED' ORDER BY d.createdAt DESC")
    List<Donation> findAllSuccessfulOrderByCreatedAtDesc();
}
