package com.retrogames.retrovault.response;

import com.retrogames.retrovault.dto.*;
import com.retrogames.retrovault.entity.*;

import java.util.List;

public record GameResponse(
        Long id,
        String title,
        String description,
        Integer releaseYear,
        LookupDTO publisher,
        LookupDTO developer,
        GameStatus status,
        String coverImage,
        List<LookupDTO> genres,
        List<LookupDTO> platforms,
        List<DownloadDTO> downloads,
        List<ImageDTO> images
) { }
