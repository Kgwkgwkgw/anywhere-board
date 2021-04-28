package com.tommy.board.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostReadResponse {
    private Long postId;
    private String boardMetaCode;
    private String title;
    private String content;
    private String nickname;
    private String accountId;
    // 인증 게시판에서만 사용된다.
    private Long likeCount;
    private Long disLikeCount;
}
