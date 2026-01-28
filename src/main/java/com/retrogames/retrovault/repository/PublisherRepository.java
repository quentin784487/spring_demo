package com.retrogames.retrovault.repository;

import com.retrogames.retrovault.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Publisher findById(Integer id);
    List<Publisher> findByNameContainingIgnoreCaseOrderByIdAsc(String name);
    Optional<Publisher> findByIdOrderByIdAsc(Long id);
}
