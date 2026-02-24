package com.retrogames.retrovault.controller;

import com.retrogames.retrovault.entity.Genre;
import com.retrogames.retrovault.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/genres")
@RequiredArgsConstructor
public class GameGenreController {
    private final GameService service;

    @GetMapping
    public List<Genre> list() {
        return service.getAllGenres();
    }
}
