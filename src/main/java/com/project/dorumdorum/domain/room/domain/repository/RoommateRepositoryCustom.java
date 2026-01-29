package com.project.dorumdorum.domain.room.domain.repository;

import com.project.dorumdorum.domain.room.application.dto.response.MyRoommateResponse;

import java.util.List;

public interface RoommateRepositoryCustom {

    List<MyRoommateResponse> findMyRoommates(Long userNo);
}
