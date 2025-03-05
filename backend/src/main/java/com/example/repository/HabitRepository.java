package com.example.repository;

import com.example.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    Habit findByName(String habitName);

    List<Habit> findByUserId(Long userId);
}
