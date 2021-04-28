package com.tommy.board.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateRequest {
    private String boardMetaCode;
    private String title;
    private String content;
    private String nickname;
    private String password;
    private String accountId;
}
