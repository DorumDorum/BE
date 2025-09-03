package com.project.dorumdorum.domain.image.application.usecase;

import com.project.dorumdorum.domain.image.application.dto.response.LoadImageResponse;
import com.project.dorumdorum.domain.image.application.dto.response.UploadImageResponse;
import com.project.dorumdorum.domain.image.domain.entity.Image;
import com.project.dorumdorum.domain.image.domain.service.ImageService;
import com.project.dorumdorum.domain.user.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ImageUseCase {

    private final UserService userService;
    private final ImageService imageService;

    public UploadImageResponse upload(Long userNo, MultipartFile file) throws IOException {
        userService.validateExistsById(userNo);
        imageService.validateFile(file);

        String originalFileName = file.getOriginalFilename();
        String newFilename = imageService.buildFilenameWithExtension(originalFileName);
        String imageKey = imageService.buildImageKey(userNo, newFilename);

        // 실제 저장될 디렉토리 경로
        Path imageDirPath = imageService.getDirPath();

        // 로컬 디스크에 저장
        imageService.saveImageInLocal(file, imageDirPath, imageKey);

        //
        return UploadImageResponse.create(imageService.saveImageEntity(
                originalFileName,
                newFilename,
                imageKey
                )
        );

    }

    public LoadImageResponse loadImage(Long imageNo) {
        Image image = imageService.findById(imageNo);

        Path imagePath = imageService.getPathByImage(image);

        Resource resource = new FileSystemResource(imagePath);

        return LoadImageResponse.create(resource, image.getOriginalImageName());
    }
}
