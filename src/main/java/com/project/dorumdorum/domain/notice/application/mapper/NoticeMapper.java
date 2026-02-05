package com.project.dorumdorum.domain.notice.application.mapper;

import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.domain.notice.domain.entity.Notice;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoticeMapper {
    NoticeResponse toResponse(Notice notice);
    List<NoticeResponse> toResponseList(List<Notice> notices);
}
