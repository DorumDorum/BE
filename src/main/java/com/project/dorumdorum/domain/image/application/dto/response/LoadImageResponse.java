package com.project.dorumdorum.domain.image.application.dto.response;

import lombok.Builder;
import org.springframework.core.io.Resource;

@Builder
public record LoadImageResponse (
        Resource resource,
        String originalFileName
) {

    public static LoadImageResponse create(Resource resource, String originalFileName) {
        return LoadImageResponse.builder()
                .resource(resource)
                .originalFileName(originalFileName)
                .build();
    }
}
