package com.retrogames.retrovault.controller;

import com.retrogames.retrovault.entity.Game;
import com.retrogames.retrovault.entity.ReturnType;
import com.retrogames.retrovault.entity.SearchMethod;
import com.retrogames.retrovault.request.GameRequest;
import com.retrogames.retrovault.response.GameResponse;
import com.retrogames.retrovault.response.LookupResponse;
import com.retrogames.retrovault.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/games")
@RequiredArgsConstructor
public class GameController {
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

    @GetMapping
    public Page<GameResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "CONTAINING") SearchMethod method,
            @RequestParam(required = false, defaultValue = "0") Integer genre,
            @RequestParam(required = false, defaultValue = "FULL") ReturnType returnType
            ) {
        return service.list(page, size, title, method, genre, returnType);
    }

    @GetMapping("/{id}")
    public GameResponse get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/all-lookups")
    public LookupResponse getAllLookups() {
        return service.getAllLookups();
    }
}
