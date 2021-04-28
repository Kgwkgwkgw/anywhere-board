package com.tommy.board.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("anonym")
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@PrimaryKeyJoinColumn(name="comment_id")
public class CommentAnonym extends Comment {
    @Column(nullable = false)
    private String hashed_password;
}
