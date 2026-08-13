package com.example.suitability.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.suitability.entity.Horse;

public interface HorseRepository extends JpaRepository<Horse, Long>{

}
