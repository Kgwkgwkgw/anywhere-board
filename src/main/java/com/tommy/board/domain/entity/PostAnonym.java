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
public class PostAnonym extends Post {
    private String hashedPassword;
}
