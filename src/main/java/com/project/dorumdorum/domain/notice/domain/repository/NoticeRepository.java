package com.project.dorumdorum.domain.notice.domain.repository;

import com.project.dorumdorum.domain.notice.domain.entity.Notice;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByRoomNo(Long roomNo);
}
