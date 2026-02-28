package com.retrogames.retrovault.mapper;

import com.retrogames.retrovault.dto.DownloadDTO;
import com.retrogames.retrovault.dto.ImageDTO;
import com.retrogames.retrovault.dto.LookupDTO;
import com.retrogames.retrovault.entity.*;
import com.retrogames.retrovault.repository.PublisherRepository;
import com.retrogames.retrovault.request.PublisherRequest;
import com.retrogames.retrovault.response.GameResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

    public final class Mappers {

    private Mappers(){}

    public static GameResponse toGameDto(Game game) {
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

    public static GameResponse toGameClientDto(Game game) {
        return new GameResponse(
                game.getId(),
                game.getTitle(),
                null,
                game.getReleaseYear(),
                null,
                null,
                game.getStatus(),
                game.getCoverImage(),
                toGenreDTO(game.getGenres()),
                toPlatformDTO(game.getPlatforms()),
                null,
                null
        );
    }

    public static List<LookupDTO> toPublisherDTO(List<Publisher> publishers) {
        List<LookupDTO> _publishers = new ArrayList<>();
        for (Publisher publisher : publishers) {
            _publishers.add(new LookupDTO(
                    publisher.getId(),
                    publisher.getName()
            ));
        }
        return _publishers;
    }

    public static LookupDTO toDeveloperDTO(Developer developer) {
        return new LookupDTO(
                developer.getId(),
                developer.getName()
        );
    }

    public static List<LookupDTO> toGenreDTO(Set<Genre> genres) {
        List<LookupDTO> _genres = new ArrayList<>();
        for (Genre genre : genres) {
            _genres.add(new LookupDTO(
                    genre.getId(),
                    genre.getName()
            ));
        }
        return _genres;
    }

    public static List<LookupDTO> toPlatformDTO(Set<Platform> platforms) {
        List<LookupDTO> _platforms = new ArrayList<>();
        for (Platform platform : platforms) {
            _platforms.add(new LookupDTO(
                    platform.getId(),
                    platform.getName()
            ));
        }
        return _platforms;
    }

    public static List<DownloadDTO> toDownloadDTO(List<Download> downloads) {
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

    public static List<ImageDTO> toImageDTO(Set<Image> images) {
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

    public static List<Publisher> toPublisherList(Set<PublisherRequest> publisherRequests, PublisherRepository publisherRepository) {
        List<Publisher> publishers = new ArrayList<>();
        for (PublisherRequest pr : publisherRequests) {
            Optional<Publisher> publisher = publisherRepository.findByIdOrderByIdAsc(pr.id());
            publishers.add(publisher.orElse(null));
        }
        return publishers;
    }
}
