package com.project.dorumdorum.domain.chat.application.usecase;

import com.project.dorumdorum.domain.chat.application.dto.response.ChatRoomMemberResponse;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoom;
import com.project.dorumdorum.domain.chat.domain.entity.ChatRoomMember;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomMemberService;
import com.project.dorumdorum.domain.chat.domain.service.ChatRoomService;
import com.project.dorumdorum.domain.roommate.domain.service.RoommateService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetChatRoomMembersUseCase {

    private final ChatRoomService chatRoomService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final RoommateService roommateService;
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<ChatRoomMemberResponse> execute(String chatRoomNo, String requestingUserNo) {
        ChatRoom chatRoom = chatRoomService.findByChatRoomNo(chatRoomNo);
        chatRoomMemberService.validateMembership(chatRoom, requestingUserNo);

        List<ChatRoomMember> members = chatRoomMemberService.findByChatRoom(chatRoom);

        return members.stream()
                .map(member -> {
                    User user = userService.findById(member.getUserNo());
                    String nickname = (user.getNickname() != null && !user.getNickname().isBlank())
                            ? user.getNickname() : user.getName();
                    boolean isHost = roommateService.isHostOfRoom(member.getUserNo(), chatRoom.getRoomNo());
                    return new ChatRoomMemberResponse(member.getUserNo(), nickname, isHost);
                })
                .sorted(Comparator.comparing(ChatRoomMemberResponse::isHost).reversed())
                .toList();
    }
}
