package com.tommy.board.domain.type;

import lombok.Getter;

@Getter
public enum AlarmType {
    NEW_COMMENT("새 댓글"),
    REPLY_COMMENT("대 댓글"),
    NEW_POST("키워드 설정된 게시글 생성 알림"),
    LIKE_MY_POST("내 게시글 좋아요"),
    DISLIKE_MY_POST("내 게시글 싫어요"),
    LIKE_MY_COMMENT("내 댓글 좋아요"),
    DISLIKE_MY_COMMENT("내 댓글 싫어요");
    private String desc;
    AlarmType(String desc) {
        this.desc = desc;
    };
}
