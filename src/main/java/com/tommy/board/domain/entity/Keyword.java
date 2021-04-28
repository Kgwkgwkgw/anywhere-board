package com.tommy.board.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Table(indexes = @Index(name = "idx_account_id_board_meta_id", columnList = "account_id, board_meta_id"))
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "keyword_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_meta_id")
    private BoardMeta boardMeta;

    @Column(nullable = false)
    private String accountId;
    private String content;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant modifiedAt;
}
