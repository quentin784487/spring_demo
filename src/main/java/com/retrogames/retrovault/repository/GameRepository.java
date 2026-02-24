package com.retrogames.retrovault.repository;

import com.retrogames.retrovault.entity.Game;
import com.retrogames.retrovault.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface GameRepository extends JpaRepository<Game, Long> {
    Page<Game> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Game> findByTitleStartingWithIgnoreCase(String title, Pageable pageable);
    Page<Game> findByGenres(Set<Genre> genres, Pageable pageable);
}
