package com.project.dorumdorum.domain.notice.service;

import com.project.dorumdorum.domain.notice.application.dto.request.UpdateNoticeRequest;
import com.project.dorumdorum.domain.notice.application.dto.request.WriteNoticeRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.NoticesResponse;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.domain.repository.NoticeRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;


    public Notice writeNotice(Long userNo, Long roomNo, WriteNoticeRequest request) {
        Notice entity = Notice.builder()
                .roomNo(roomNo)
                .userNo(userNo)
                .title(request.title())
                .content(request.content())
                .build();

        return noticeRepository.save(entity);
    }


    public List<NoticesResponse> loadNoticeList(Long roomNo) {
        List<Notice> notices = noticeRepository.findByRoomNo(roomNo);

        return notices.stream()
                .map(NoticesResponse::create)
                .toList();
    }

    public Notice findById(@NotNull Long noticeNo) {
        return noticeRepository.findById(noticeNo)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus.NOTICE_NOT_FOUND));
    }

    public Notice updateNotice(Notice notice, UpdateNoticeRequest request) {
        notice.update(request.title(), request.content());
        return notice;
    }

    public void softDelete(Notice notice) {
        notice.delete();
        noticeRepository.save(notice);
    }

    public void deleteNotice(Long noticeNo) {
        Notice notice = findById(noticeNo);
        softDelete(notice);
    }
}
