package com.project.dorumdorum.global.security;

import com.project.dorumdorum.domain.chat.domain.entity.MessageRoom;
import com.project.dorumdorum.domain.chat.domain.entity.MessageRoomStatus;
import com.project.dorumdorum.domain.chat.domain.entity.Participant;
import com.project.dorumdorum.domain.chat.domain.repository.MessageRoomRepository;
import com.project.dorumdorum.domain.chat.domain.repository.ParticipantRepository;
import com.project.dorumdorum.domain.presence.domain.service.PresenceService;
import com.project.dorumdorum.domain.user.domain.entity.User;
import com.project.dorumdorum.domain.user.domain.repository.UserRepository;
import com.project.dorumdorum.global.exception.RestApiException;
import com.project.dorumdorum.global.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private static final Pattern ROOM_DESTINATION_PATTERN = Pattern.compile("^/sub/rooms/([A-Za-z0-9_-]+)$");

    private final TokenProvider tokenProvider;
    private final ParticipantRepository participantRepository;
    private final MessageRoomRepository messageRoomRepository;
    private final UserRepository userRepository;
    private final PresenceService presenceService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            handleConnect(accessor);
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            handleSubscribe(accessor);
            touchWsActivity(accessor);
        } else if (StompCommand.SEND.equals(accessor.getCommand())) {
            touchWsActivity(accessor);
        }

        return message;
    }

    private void handleConnect(StompHeaderAccessor accessor) {
        String authHeader = accessor.getFirstNativeHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
        }

        String token = authHeader.substring(BEARER.length());
        if (!tokenProvider.validateToken(token) || !tokenProvider.isAccessToken(token)) {
            throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
        }

        String userId = tokenProvider.getId(token)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._UNAUTHORIZED));
        accessor.setUser(new UserIdPrincipal(userId));
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        if (destination == null) {
            return;
        }

        // /sub/rooms/{roomId} 패턴 검증
        Matcher matcher = ROOM_DESTINATION_PATTERN.matcher(destination);
        if (!matcher.matches()) {
            return; // 채팅방 구독이 아닌 경우 검증 생략
        }

        // 정규식에서 첫 번째 괄호로 캡처된 값 추출 (그게 roomId)
        String roomId = matcher.group(1);
        UserIdPrincipal principal = (UserIdPrincipal) accessor.getUser();
        
        if (principal == null) {
            throw new RestApiException(GlobalErrorStatus._UNAUTHORIZED);
        }

        String userId = principal.getUserId();

        // 채팅방 상태 검증 (APPROVED 여부)
        MessageRoom messageRoom = messageRoomRepository.findById(roomId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._BAD_REQUEST));

        if (messageRoom.getRoomStatus() != MessageRoomStatus.APPROVED) {
            throw new RestApiException(GlobalErrorStatus._FORBIDDEN);
        }

        // 참여자 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(GlobalErrorStatus._UNAUTHORIZED));

        Participant participant = participantRepository.findByUserAndMessageRoomNo(user, roomId);
        
        if (participant == null || participant.getDeletedAt() != null) {
            throw new RestApiException(GlobalErrorStatus._FORBIDDEN);
        }
    }

    private void touchWsActivity(StompHeaderAccessor accessor) {
        if (accessor.getUser() instanceof UserIdPrincipal principal) {
            presenceService.onWsActivity(principal.getUserId());
        }
    }
}
