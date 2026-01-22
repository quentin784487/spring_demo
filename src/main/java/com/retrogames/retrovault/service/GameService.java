package com.retrogames.retrovault.service;

import com.retrogames.retrovault.dto.*;
import com.retrogames.retrovault.entity.*;
import com.retrogames.retrovault.repository.*;
import com.retrogames.retrovault.request.GameRequest;
import com.retrogames.retrovault.request.DownloadRequest;
import com.retrogames.retrovault.request.ImageRequest;
import com.retrogames.retrovault.request.PublisherRequest;
import com.retrogames.retrovault.response.GameResponse;
import com.retrogames.retrovault.response.LookupResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public Page<GameResponse> list(int page, int size, String title) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending()
        );

        Page<Game> games = title.equals("none")
                ? gameRepository.findAll(pageable)
                : gameRepository.findByTitleContainingIgnoreCase(title, pageable);

        return games.map(this::toGameListDto);
    }

    private GameResponse toGameListDto(Game game) {
        return new GameResponse(
                game.getId(),
                game.getTitle(),
                game.getDescription(),
                game.getReleaseYear(),
                toPublisherDTO(game.getPublishers()),
                toDeveloperDTO(game.getDeveloper()),
                game.getStatus(),
                game.getCoverImage(),
                toGenreDTO(game.getGenres()),
                toPlatformDTO(game.getPlatforms()),
                toDownloadDTO(game.getDownloads()),
                toImageDTO(game.getImages())
        );
    }

    private List<LookupDTO> toPublisherDTO(Set<Publisher> publishers) {
        List<LookupDTO> _publishers = new ArrayList<>();
        for (Publisher publisher : publishers) {
            _publishers.add(new LookupDTO(
                    publisher.getId(),
                    publisher.getName()
            ));
        }
        return _publishers;
    }

    private LookupDTO toDeveloperDTO(Developer developer) {
        return new LookupDTO(
                developer.getId(),
                developer.getName()
        );
    }

    private List<LookupDTO> toGenreDTO(Set<Genre> genres) {
        List<LookupDTO> _genres = new ArrayList<>();
        for (Genre genre : genres) {
            _genres.add(new LookupDTO(
                genre.getId(),
                genre.getName()
            ));
        }
        return _genres;
    }

    private List<LookupDTO> toPlatformDTO(Set<Platform> platforms) {
        List<LookupDTO> _platforms = new ArrayList<>();
        for (Platform platform : platforms) {
            _platforms.add(new LookupDTO(
                    platform.getId(),
                    platform.getName()
            ));
        }
        return _platforms;
    }

    private List<DownloadDTO> toDownloadDTO(Set<Download> downloads) {
        List<DownloadDTO> _downloads = new ArrayList<>();
        for (Download download : downloads) {
            _downloads.add(new DownloadDTO(
                    download.getId(),
                    download.getName(),
                    download.getType(),
                    download.getDownloadUrl()
            ));
        }
        return _downloads;
    }

    private List<ImageDTO> toImageDTO(Set<Image> images) {
        List<ImageDTO> _images = new ArrayList<>();
        for (Image download : images) {
            _images.add(new ImageDTO(
                    download.getId(),
                    download.getImage(),
                    download.getFileName()
            ));
        }
        return _images;
    }

    private Set<Publisher> toPublisherSet(Set<PublisherRequest> publisherRequests) {
        Set<Publisher> publishers = new java.util.HashSet<>();
        for (PublisherRequest pr : publisherRequests) {
            Optional<Publisher> publisher = publisherRepository.findById(pr.id());
            publishers.add(publisher.orElse(null));
        }
        return publishers;
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
        return publisherRepository.findByNameContainingIgnoreCase(name);
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

        Developer developer = developerRepository.findById(request.developer());

        if (developer != null) {
            game.setDeveloper(developer);
        }

        Set<Publisher> publishers = toPublisherSet(request.publishers());

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

        Set<Publisher> publishers = toPublisherSet(request.publishers());

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
