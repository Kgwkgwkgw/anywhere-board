package com.tommy.board.domain.type;


public enum PreferenceType {
    LIKE("좋아요"),
    DISLIKE("싫어요");

    private String desc;

    PreferenceType(String desc) {
        this.desc = desc;
    }

}
