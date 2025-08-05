package com.tsinjo.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.tsinjo.demo.model.Help;

import java.util.List;

@Repository
public interface HelpRepository extends JpaRepository<Help, Long> {
    @Query("SELECT h FROM Help h WHERE h.payment.status = 'SUCCEEDED' ORDER BY h.createdAt DESC")
    List<Help> findAllSuccessfulOrderByCreatedAtDesc();
}