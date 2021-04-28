package com.tommy.board.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("certification")
@JsonIgnoreProperties(ignoreUnknown = true)
//@Table(indexes = @Index(name = "idx_account_id", columnList = "account_id"))
@Builder
public class PostCertification extends Post {
    private String accountId;
}
