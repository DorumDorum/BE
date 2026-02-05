package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.mapper.NoticeMapper;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.domain.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadNoticesUseCase {

    private final NoticeRepository noticeRepository;
    private final NoticeMapper noticeMapper;

    public List<NoticeResponse> execute() {
        List<Notice> notices = noticeRepository.findAllByOrderByWrittenDateDesc();
        return noticeMapper.toResponseList(notices);
    }
}
