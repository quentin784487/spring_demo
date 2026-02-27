package com.retrogames.retrovault.service;

import com.retrogames.retrovault.dto.*;
import com.retrogames.retrovault.entity.*;
import com.retrogames.retrovault.repository.*;
import com.retrogames.retrovault.request.*;
import com.retrogames.retrovault.mapper.*;
import com.retrogames.retrovault.response.GameResponse;
import com.retrogames.retrovault.response.LookupResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final DeveloperRepository developerRepository;
    private final PublisherRepository publisherRepository;

    public Page<GameResponse> list(int page,
                                   int size,
                                   String title,
                                   SearchMethod method,
                                   Integer genre,
                                   ReturnType returnType) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<Game> games;

        if (method.equals(SearchMethod.CONTAINING) && !title.isEmpty()) {
            games = gameRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (method.equals(SearchMethod.STARTSWITH) && !title.isEmpty()) {
            games = gameRepository.findByTitleStartingWithIgnoreCase(title, pageable);
        } else if (genre > 0) {
            Set<Genre> _genres = genreRepository.findById(genre);
            games = gameRepository.findByGenres(_genres, pageable);
        } else {
            games = gameRepository.findAll(pageable);
        }

        if (returnType.equals(ReturnType.FULL))
            return games.map(Mappers::toGameDto);
        else
            return games.map(Mappers::toGameClientDto);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public List<Platform> getAllPlatforms() {
        return platformRepository.findAll();
    }

    public List<Developer> getDevelopersContainingName(String name) {
        return developerRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Publisher> getPublishersContainingName(String name) {
        return publisherRepository.findByNameContainingIgnoreCaseOrderByIdAsc(name);
    }

    public LookupResponse getAllLookups() {
        return new LookupResponse(
                genreRepository.findAll(),
                platformRepository.findAll()
        );
    }

    public GameResponse get(Long id) {
        Game game = gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));
        return Mappers.toGameDto(game);
    }

    public void create(GameRequest request) {
        Game game = new Game();
        game.setTitle(request.title());
        game.setDescription(request.description());
        game.setReleaseYear(request.releaseYear());

        Developer developer = developerRepository.findById(request.developer());

        if (developer != null) {
            game.setDeveloper(developer);
        }

        List<Publisher> publishers = Mappers.toPublisherList(request.publishers(), publisherRepository);

        if (!publishers.isEmpty()) {
            game.setPublishers(publishers);
        }

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
                download.setDownloadUrl(d.link());
                download.setName(d.name());
                download.setType(d.type());
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

        Developer developer = developerRepository.findById(request.developer());

        if (developer != null) {
            game.setDeveloper(developer);
        }

        List<Publisher> publishers = Mappers.toPublisherList(request.publishers(), publisherRepository);

        game.getPublishers().clear();

        if (!publishers.isEmpty()) {
            game.setPublishers(publishers);
        }

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
                download.setDownloadUrl(d.link());
                download.setName(d.name());
                download.setType(d.type());
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
