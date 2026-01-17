package com.retrogames.retrovault.request;

import com.retrogames.retrovault.entity.GameStatus;
import java.util.Set;

public record GameRequest(
        String title,
        String description,
        Integer releaseYear,
        String developer,
        String publisher,
        GameStatus status,
        String coverImage,
        Integer genre,
        Integer platform,
        Set<DownloadRequest> downloads,
        Set<ImageRequest> images
) {
}
