package com.project.dorumdorum.domain.notice.domain.service;

import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public List<Notice> loadAllByWrittenDateDesc() {
        return noticeRepository.findAllByOrderByWrittenDateDesc();
    }
}
