package com.tommy.board.domain.type;
import lombok.Getter;
import lombok.Setter;
@Getter
public enum BoardMetaType {
    anonym("익명"),
    certification("인증된 사용자");
    private String desc;
    BoardMetaType(String desc) {
        this.desc = desc;
    }

}
