package com.retrogames.retrovault.request;

import com.retrogames.retrovault.dto.LookupDTO;
import java.util.Set;

public record GenreRequest(
        Set<LookupDTO> genres
) { }
