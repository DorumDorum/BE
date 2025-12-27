package com.project.dorumdorum.domain.image.domain.repository;

import com.project.dorumdorum.domain.image.domain.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByNoticeNo(Long noticeNo);

    long deleteByNoticeNo(Long noticeNo);
}

