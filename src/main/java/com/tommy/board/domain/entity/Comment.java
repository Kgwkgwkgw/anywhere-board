package com.tommy.board.domain.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(indexes = {
        @Index(name = "idx_upper_comment_id", columnList = "upper_comment_id"),
        @Index(name = "idx_post_id", columnList = "post_id")})
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upper_comment_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> childList;

    private String content;
    private String nickname;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant modifiedAt;

}
