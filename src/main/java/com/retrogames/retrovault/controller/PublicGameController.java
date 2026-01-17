package com.retrogames.retrovault.controller;

import com.retrogames.retrovault.entity.Game;
import com.retrogames.retrovault.request.GameRequest;
import com.retrogames.retrovault.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/games")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class PublicGameController {
    private final GameService service;

    @PostMapping
    public void create(@RequestBody GameRequest game) {
        service.create(game);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody GameRequest game) {
        service.update(id, game);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
