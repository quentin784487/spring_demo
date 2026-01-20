package com.retrogames.retrovault.repository;

import com.retrogames.retrovault.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    Publisher findById(Integer id);
    List<Publisher> findByNameContainingIgnoreCase(String name);
}
