package com.project.dorumdorum.domain.image.application.dto.response;

import com.project.dorumdorum.domain.image.domain.entity.Image;

import lombok.Builder;

@Builder
public record LoadImageResponse (
        Long imageNo,
        String imageKey,
        String publicUrl
) {

    public static LoadImageResponse create(Image image, String publicUrl) {
        return LoadImageResponse.builder()
                .imageNo(image.getImageNo())
                .imageKey(image.getImageKey())
                .publicUrl(publicUrl)
                .build();
    }
}
