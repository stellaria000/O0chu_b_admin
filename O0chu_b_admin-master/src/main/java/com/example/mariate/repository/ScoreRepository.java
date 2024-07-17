package com.example.mariate.repository;

import com.example.mariate.entity.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<ScoreEntity, Integer> {
}