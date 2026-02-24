package com.retrogames.retrovault.controller;

import com.retrogames.retrovault.entity.Platform;
import com.retrogames.retrovault.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/platforms")
@RequiredArgsConstructor
public class GamePlatformController {
    private final GameService service;

    @GetMapping
    public List<Platform> list() {
        return service.getAllPlatforms();
    }
}
