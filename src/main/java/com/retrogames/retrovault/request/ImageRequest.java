package com.retrogames.retrovault.request;

public record ImageRequest(
        String imageBase64,
        String fileName
) {
}
