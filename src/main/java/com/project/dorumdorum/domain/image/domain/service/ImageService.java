package com.project.dorumdorum.domain.image.domain.service;

import com.project.dorumdorum.domain.image.domain.entity.Image;
import com.project.dorumdorum.domain.image.domain.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public Image saveNoticeImage(Long noticeNo, String s3Key, String fileName, Long fileSize) {
        Image image = Image.builder()
                .noticeNo(noticeNo)
                .s3Key(s3Key)
                .fileName(fileName)
                .fileSize(fileSize)
                .build();
        return imageRepository.save(image);
    }

    @Transactional(readOnly = true)
    public Optional<Image> findByNoticeNo(Long noticeNo) {
        return imageRepository.findByNoticeNo(noticeNo);
    }

    @Transactional
    public void softDelete(Image image) {
        image.delete();
        imageRepository.save(image);
    }

    @Transactional
    public void deleteByNoticeNo(Long noticeNo) {
        imageRepository.deleteByNoticeNo(noticeNo);
    }
}

