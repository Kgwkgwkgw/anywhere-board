package com.tommy.board.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = @Index(name = "idx_board_meta_id", columnList = "board_meta_id"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_meta_id")
    private BoardMeta boardMeta;

    private String title;
    private String content;
    private String nickname;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant modifiedAt;
}
