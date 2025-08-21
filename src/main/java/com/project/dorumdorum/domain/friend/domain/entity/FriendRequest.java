package com.project.dorumdorum.domain.friend.domain.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class FriendRequest {

    @Id @Tsid
    private Long friendRequestNo;

    @Column(name = "from_user", nullable = false)
    private Long fromUser;

    @Column(name = "to_user", nullable = false)
    private Long toUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendRequestStatus status;

    public void acceptRequest() {
        this.status = FriendRequestStatus.ACCEPTED;
    }

    public void rejectRequest() {
        this.status = FriendRequestStatus.REJECTED;
    }


    public boolean isReceiver(Long toUser) {
        return this.toUser.equals(toUser);
    }

    public boolean isPendingStatus() {
        return this.status.equals(FriendRequestStatus.PENDING);
    }

    public boolean isSender(Long fromUser) {
        return this.fromUser.equals(fromUser);
    }
}
