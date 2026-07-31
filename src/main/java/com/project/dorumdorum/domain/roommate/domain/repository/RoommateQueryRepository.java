package com.project.dorumdorum.domain.roommate.domain.repository;

import com.project.dorumdorum.domain.roommate.application.dto.response.MyRoommateResponse;
import com.project.dorumdorum.domain.roommate.application.dto.response.RoommateHistoryResponse;

import java.util.List;

public interface RoommateQueryRepository {

    List<MyRoommateResponse> findMyRoommates(String userNo);

    List<RoommateHistoryResponse> findMyRoommateHistory(String userNo);
}
