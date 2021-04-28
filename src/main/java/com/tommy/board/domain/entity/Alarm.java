package com.tommy.board.domain.entity;

import com.tommy.board.domain.type.AlarmType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = @Index(name = "idx_alarm_account_id_board_meta_id_is_deleted", columnList = "account_id, board_meta_id, is_deleted"))
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_meta_id")
    private BoardMeta boardMeta;

    @Column(nullable = false, name = "account_id")
    private String accountId;
    @Enumerated(EnumType.STRING)
    AlarmType type;
    private boolean isRead;
    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private AlarmExtraInfo extraInfo;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant modifiedAt;
    @Builder
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlarmExtraInfo {
        private Long postId;
        private String postAccountId;
        private String postNickname;
        private Long commentId;
        private String commentAccountId;
        private String commentNickname;
        private Long upperCommentId;
        private String upperCommentNickname;
        private String actionAccountId;
        private String actionNickname;
    }
}
