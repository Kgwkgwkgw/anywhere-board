package com.tommy.board.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("certification")
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(indexes = @Index(name = "idx_comment_certification_account_id", columnList = "account_id"))
@Builder
@PrimaryKeyJoinColumn(name = "comment_id")
public class CommentCertification extends Comment {
    @Column(nullable = false, name = "account_id")
    private String accountId;
}
