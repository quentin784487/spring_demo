package com.retrogames.retrovault.controller;

import com.retrogames.retrovault.entity.Developer;
import com.retrogames.retrovault.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/developers")
@RequiredArgsConstructor
public class GameDeveloperController {
    private final GameService service;

    @GetMapping()
    public List<Developer> getDevelopers(@RequestParam(defaultValue = "") String name) {
        return service.getDevelopersContainingName(name);
    }
}
