package com.retrogames.retrovault.service;

import com.retrogames.retrovault.entity.*;
import com.retrogames.retrovault.repository.GameRepository;
import com.retrogames.retrovault.repository.GenreRepository;
import com.retrogames.retrovault.repository.PlatformRepository;
import com.retrogames.retrovault.request.GameRequest;
import com.retrogames.retrovault.request.DownloadRequest;
import com.retrogames.retrovault.request.ImageRequest;
import com.retrogames.retrovault.response.LookupResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;

    public Page<Game> list(int page, int size, String title) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending()
        );

        return title.equals("none")
                ? gameRepository.findAll(pageable)
                : gameRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public List<Platform> getAllPlatforms() {
        return platformRepository.findAll();
    }

    public LookupResponse getAllLookups() {
        return new LookupResponse(
                genreRepository.findAll(),
                platformRepository.findAll()
        );
    }

    public Game get(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found"));
    }

    public void create(GameRequest request) {
        Game game = new Game();
        game.setTitle(request.title());
        game.setDescription(request.description());
        game.setReleaseYear(request.releaseYear());
        game.setDeveloper(request.developer());
        game.setPublisher(request.publisher());
        game.setStatus(request.status());
        game.setCoverImage(request.coverImage());

        // Genres

        Set<Genre> genres = genreRepository.findById(request.genre());

        if (!genres.isEmpty()) {
            game.setGenres(genres);
        }

        // Platforms

        Set<Platform> platforms = platformRepository.findById(request.platform());

        if (!platforms.isEmpty()) {
            game.setPlatforms(platforms);
        }

        // Downloads
        if (request.downloads() != null) {
            for (DownloadRequest d : request.downloads()) {
                Download download = new Download();
                download.setDownloadUrl(d.downloadUrl());
                game.addDownload(download);
            }
        }

        //Images
        if (request.images() != null) {
            for (ImageRequest i : request.images()) {
                Image image = new Image();
                image.setImage(i.imageBase64());
                image.setFileName(i.fileName());
                game.addImage(image);
            }
        }

        gameRepository.save(game);
    }

    public void update(Long id, GameRequest request) {

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        game.setTitle(request.title());
        game.setDescription(request.description());
        game.setReleaseYear(request.releaseYear());
        game.setDeveloper(request.developer());
        game.setPublisher(request.publisher());
        game.setStatus(request.status());
        game.setCoverImage(request.coverImage());

        // Genres

        Set<Genre> genres = genreRepository.findById(request.genre());

        game.getGenres().clear();

        if (!genres.isEmpty()) {
            game.setGenres(genres);
        }

        // Platforms

        Set<Platform> platforms = platformRepository.findById(request.platform());

        game.getPlatforms().clear();

        if (!platforms.isEmpty()) {
            game.setPlatforms(platforms);
        }

        // Downloads
        game.getDownloads().clear();
        if (request.downloads() != null) {
            for (DownloadRequest d : request.downloads()) {
                Download download = new Download();
                download.setDownloadUrl(d.downloadUrl());
                game.addDownload(download);
            }
        }

        //Images
        game.getImages().clear();
        if (request.images() != null) {
            for (ImageRequest i : request.images()) {
                Image image = new Image();
                image.setImage(i.imageBase64());
                image.setFileName(i.fileName());
                game.addImage(image);
            }
        }

        gameRepository.save(game);
    }

    public void delete(Long id) {
        gameRepository.deleteById(id);
    }
}
