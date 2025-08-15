package com.project.dorumdorum.domain.friend.domain.entity;

import com.project.dorumdorum.domain.user.domain.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user", nullable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user", nullable = false)
    private User toUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendRequestStatus status;

    public void acceptRequest(Long requestNo) {
        this.status = FriendRequestStatus.ACCEPTED;
    }

    public void rejectRequest(Long requestNo) {
        this.status = FriendRequestStatus.REJECTED;
    }

}
