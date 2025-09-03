package com.project.dorumdorum.domain.image.ui;

import com.project.dorumdorum.domain.image.application.dto.response.LoadImageResponse;
import com.project.dorumdorum.domain.image.application.dto.response.UploadImageResponse;
import com.project.dorumdorum.domain.image.application.usecase.ImageUseCase;
import com.project.dorumdorum.global.annotation.CurrentUser;
import com.project.dorumdorum.global.common.BaseResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController { // 테스트 용도

    private final ImageUseCase imageUseCase;

    @PostMapping(value = "/api/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<UploadImageResponse> upload(
            @CurrentUser Long userNo,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        return BaseResponse.onSuccess(imageUseCase.upload(userNo, file));
    }

    @GetMapping("/api/image")
    public ResponseEntity<Resource> loadImage(@RequestParam Long imageNo) {
        LoadImageResponse response = imageUseCase.loadImage(imageNo);

        MediaType mediaType = MediaTypeFactory.getMediaType(response.originalFileName())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + response.originalFileName() + "\"")
                .body(response.resource());
    }
}
