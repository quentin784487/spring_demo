package com.retrogames.retrovault.response;

import com.retrogames.retrovault.entity.Genre;
import com.retrogames.retrovault.entity.Platform;

import java.util.List;

public record LookupResponse(
        List<Genre> genres,
        List<Platform> platforms
) {
}
