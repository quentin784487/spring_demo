package com.retrogames.retrovault.dto;

import com.retrogames.retrovault.entity.DownloadType;

public record DownloadDTO(
        Long id,
        String name,
        DownloadType type,
        String downloadUrl
) {
}
