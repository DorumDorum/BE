package com.project.dorumdorum.domain.notice.service;

import com.project.dorumdorum.domain.notice.application.dto.request.WriteNoticeRequest;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import com.project.dorumdorum.domain.notice.domain.repository.NoticeRepository;
import com.project.dorumdorum.domain.room.domain.entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;


    public Notice writeNotice(Long userNo, Room room, WriteNoticeRequest request) {
        Notice entity = Notice.builder()
                .room(room)
                .userNo(userNo)
                .title(request.title())
                .content(request.content())
                .build();

        return noticeRepository.save(entity);
    }



}
