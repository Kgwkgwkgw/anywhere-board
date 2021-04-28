package com.tommy.board.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardMetaUpdateRequest {
    private Long id;
    private String name;
}
