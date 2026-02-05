package com.project.dorumdorum.domain.roommate.domain.repository;

import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;

import java.util.List;

public interface RoommateRepositoryCustom {

    List<MyRoommateResponse> findMyRoommates(String userNo);
}
