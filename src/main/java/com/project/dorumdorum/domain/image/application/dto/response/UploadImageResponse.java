package com.project.dorumdorum.domain.image.application.dto.response;

import com.project.dorumdorum.domain.image.domain.entity.Image;

import lombok.Builder;

@Builder
public record UploadImageResponse(
        Long imageNo,
        String imageKey
) {
    public static UploadImageResponse create(Image image) {
        return UploadImageResponse.builder()
                .imageNo(image.getImageNo())
                .imageKey(image.getImageKey())
                .build();
    }
}
