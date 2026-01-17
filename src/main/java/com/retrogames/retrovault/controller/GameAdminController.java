package com.retrogames.retrovault.controller;

import com.retrogames.retrovault.entity.Game;
import com.retrogames.retrovault.response.LookupResponse;
import com.retrogames.retrovault.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/games")
@RequiredArgsConstructor
public class GameAdminController {
    private final GameService service;

    @GetMapping
    public Page<Game> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title
    ) {
        return service.list(page, size, title);
    }

    @GetMapping("/{id}")
    public Game get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/all-lookups")
    public LookupResponse getAllLookups() {
        return service.getAllLookups();
    }
}
