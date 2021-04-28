package com.tommy.board.domain.type;

public enum PreferenceDataType {
    POST("게시판"),
    COMMENT("댓글");
    private String desc;

    PreferenceDataType(String desc) {
        this.desc = desc;
    }
}
