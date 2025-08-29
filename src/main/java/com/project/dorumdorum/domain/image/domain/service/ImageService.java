package com.project.dorumdorum.domain.image.domain.service;

import com.project.dorumdorum.domain.image.domain.entity.ContentType;
import com.project.dorumdorum.domain.image.domain.entity.Image;
import com.project.dorumdorum.domain.image.repository.ImageRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${app.storage.local.base-dir:./uploads}")
    private String baseDir;

    @Value("${app.storage.local.public-base-url:http://localhost:8080/files}")
    private String publicBaseUrl;

    private final ImageRepository imageRepository;

    public void validateFile(MultipartFile file) {
        if(file == null || file.isEmpty())
            throw new IllegalArgumentException("File is empty");
        long max = 5L * 1024 * 1024; //5MB
        if (file.getSize() > max)
            throw new IllegalArgumentException("File is too large");
        String contentType = Optional.ofNullable(file.getContentType()).orElse("");

        //throw new IllegalArgumentException("Invalid content type");
    }

    public String buildFilenameWithExtension(String originalFilename) {
        String extension = "";
        if(StringUtils.hasText(originalFilename) && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID() + (StringUtils.hasText(extension) ? extension : "");
    }

    public Path getDirPath() throws IOException {
        Path imagesDir = Path.of(baseDir, "images").toAbsolutePath().normalize();
        Files.createDirectories(imagesDir);
        return imagesDir;
    }

    public String buildImageKey(Long userNo, String newFilename) {
        return "image/" + userNo + "/" + newFilename;
    }

    public void saveImageInLocal(MultipartFile file, Path imageDirPath, String newFilename) throws IOException {
        Path target = imageDirPath.resolve(newFilename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
    }

    public Image saveImageEntity(String originalFileName, String newFilename, String path) {
        Image newImage = Image.builder()
                .originalImageName(originalFileName)
                .storedImageName(newFilename)
                .imageKey(path)
                .build();
        return imageRepository.save(newImage);
    }

    public Image findById(Long imageNo) {
        return imageRepository.findById(imageNo)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._NOT_FOUND));
    }

    public String buildPublicUrl(Image image) {
        return publicBaseUrl + "/" + image.getImageKey();
    }
}
