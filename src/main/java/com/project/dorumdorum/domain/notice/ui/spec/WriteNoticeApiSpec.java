package com.project.dorumdorum.domain.notice.ui.spec;

import com.project.dorumdorum.domain.notice.application.dto.request.WriteNoticeRequest;
import com.project.dorumdorum.domain.notice.application.dto.response.NoticeResponse;
import com.project.dorumdorum.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Notice")
public interface WriteNoticeApiSpec {

    @Operation(
            summary = "공지 작성",
            description = "공지 생성 및 이미지 업로드 presigned URL 발급 (이미지 1개). "
                    + "요청에 imageFileName, imageFileSize를 주면 업로드 URL이 응답에 포함됩니다."
    )
    @PostMapping("/api/notice")
    BaseResponse<NoticeResponse> writeNotice(
            @Parameter(hidden = true)
            Long userNo,
            @RequestBody(
                    description = """
                            공지 작성 요청
                            - roomNo: 방 번호
                            - title, content: 제목/내용
                            - imageFileName: 업로드할 이미지 파일명
                            - imageFileSize: 업로드할 이미지 크기(byte)
                            """,
                    required = true
            )
            WriteNoticeRequest request
    );
}

