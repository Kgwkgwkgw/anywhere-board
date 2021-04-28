package com.tommy.board.domain.dto;

import com.tommy.board.domain.entity.BoardMeta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostReadRequest {
    private Long postId;
    private String boardMetaCode;
}
