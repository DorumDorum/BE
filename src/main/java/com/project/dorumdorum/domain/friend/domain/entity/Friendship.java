package com.project.dorumdorum.domain.friend.domain.entity;


import com.project.dorumdorum.domain.user.domain.entity.User;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "frinedship",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_friendship_pair",
                        columnNames = {"user_no", "friend_user_no"}
                )
        },
        indexes = {
                @Index(name = "idx_friendship_user", columnList = "user_no"),
                @Index(name = "idx_friendship_friend", columnList = "friend_user_no")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Friendship {
    @Id @Tsid
    private Long friendshipNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no", nullable = false)
    private User userNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_user_no", nullable = false)
    private User friendUserNo;
}
