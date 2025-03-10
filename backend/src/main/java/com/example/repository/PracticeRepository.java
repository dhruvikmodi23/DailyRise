package com.example.repository;

import com.example.model.PracticeTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<PracticeTracker, Long> {
    List<PracticeTracker> findByDate(LocalDate date);
    List<PracticeTracker> findByUserId(Long userId);
}
