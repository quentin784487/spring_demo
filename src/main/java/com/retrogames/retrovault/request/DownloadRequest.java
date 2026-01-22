package com.retrogames.retrovault.request;

import com.retrogames.retrovault.entity.DownloadType;

public record DownloadRequest(
        String name,
        DownloadType type,
        String link
) { }
