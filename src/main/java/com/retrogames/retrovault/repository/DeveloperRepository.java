package com.retrogames.retrovault.repository;

import com.retrogames.retrovault.entity.Developer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {
    Developer findById(Integer id);
    List<Developer> findByNameContainingIgnoreCase(String name);
}
