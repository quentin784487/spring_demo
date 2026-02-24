package com.retrogames.retrovault.controller;

import com.retrogames.retrovault.entity.Publisher;
import com.retrogames.retrovault.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/publishers")
@RequiredArgsConstructor
public class GamePublisherController {
    private final GameService service;

    @GetMapping()
    public List<Publisher> list(@RequestParam(defaultValue = "") String name) {
        return service.getPublishersContainingName(name);
    }
}
