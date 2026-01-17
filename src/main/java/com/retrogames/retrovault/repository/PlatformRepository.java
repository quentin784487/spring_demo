package com.retrogames.retrovault.repository;

import com.retrogames.retrovault.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface PlatformRepository extends JpaRepository<Platform, Long> {
    Optional<Platform> findByNameIgnoreCase(String name);
    Set<Platform> findById(Integer id);
}
