package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.application.mapper.NoticeMapper;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.domain.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadNoticesUseCase {

    private final NoticeService noticeService;
    private final NoticeMapper noticeMapper;

    /**
     * 공지사항 목록 조회
     * - 작성일 내림차순으로 공지사항을 조회
     * - 응답 DTO 목록으로 변환해 반환
     */
    public List<NoticeResponse> execute() {
        List<Notice> notices = noticeService.loadAllByWrittenDateDesc();
        return noticeMapper.toResponseList(notices);
    }
}
