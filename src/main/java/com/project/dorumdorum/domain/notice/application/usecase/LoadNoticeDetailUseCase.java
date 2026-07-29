package com.project.dorumdorum.domain.notice.application.usecase;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.domain.repository.NoticeRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.dorumdorum.global.exception.code.status.CommonErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class LoadNoticeDetailUseCase {

    private final NoticeRepository noticeRepository;

    @Transactional(readOnly = true)
    public NoticeResponse execute(String noticeNo) {
        Notice notice = noticeRepository.findById(noticeNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        return new NoticeResponse(
                notice.getNoticeNo(),
                notice.getTitle(),
                notice.getContent(),
                notice.getWrittenDate(),
                notice.getOriginalLink()
        );
    }
}
