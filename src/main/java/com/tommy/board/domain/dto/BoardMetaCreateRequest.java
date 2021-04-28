package com.tommy.board.domain.dto;

import com.tommy.board.domain.type.BoardMetaType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardMetaCreateRequest {
    private String code;
    private String name;
    private BoardMetaType boardMetaType;
}
