package com.project.dorumdorum.domain.notice.ui.spec;

import com.project.dorumdorum.domain.notice.application.dto.request.UpdateNoticeRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PatchMapping;

@Tag(name = "Notice")
public interface UpdateNoticeApiSpec {

    @Operation(
            summary = "공지 수정",
            description = """
                    공지 내용 수정 및 이미지 교체/삭제
                    - 새 이미지 업로드: imageFileName + imageFileSize 제공 → 새 presigned URL 발급
                    - 기존 이미지 삭제: deleteImage=true
                    """
    )
    @PatchMapping("/api/notice")
    BaseResponse<NoticeResponse> updateNotice(
            @Parameter(hidden = true)
            Long userNo,
            @RequestBody(
                    description = """
                            공지 수정 요청
                            - roomNo, noticeNo: 식별자
                            - title, content: 수정 내용
                            - imageFileName/imageFileSize: 새 이미지 교체 시
                            - deleteImage: 기존 이미지만 삭제 시 true
                            """,
                    required = true
            )
            UpdateNoticeRequest request
    );
}

