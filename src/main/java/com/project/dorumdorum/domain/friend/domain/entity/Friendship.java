package com.project.dorumdorum.domain.friend.domain.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "friendship",
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

    @Column(name = "user_no", nullable = false)
    private Long userNo;

    @Column(name = "friend_user_no", nullable = false)
    private Long friendUserNo;
}
