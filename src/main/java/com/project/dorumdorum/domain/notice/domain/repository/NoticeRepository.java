package com.project.dorumdorum.domain.notice.domain.repository;


import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
