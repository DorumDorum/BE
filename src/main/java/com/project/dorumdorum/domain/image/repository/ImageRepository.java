package com.project.dorumdorum.domain.image.repository;

import com.project.dorumdorum.domain.image.domain.entity.Image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
