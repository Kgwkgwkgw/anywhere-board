package com.tommy.board.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostsReadResponse {
    Long total;
    Long offset;
    Long limit;
    List<PostReadResponse> postReadResponseList;
}
