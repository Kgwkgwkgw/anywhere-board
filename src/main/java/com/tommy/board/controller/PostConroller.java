package com.tommy.board.controller;

import com.tommy.board.domain.dto.PostsReadResponse;
import com.tommy.board.domain.type.PostOrderType;
import com.tommy.board.service.impl.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostConroller {
    private PostServiceImpl postService;

    @Autowired
    public PostConroller(PostServiceImpl postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public PostsReadResponse getPostsByBoardMetaCode(@RequestParam String boardMetaCode, @RequestParam(required = false) Long lastPostId, @RequestParam(required = false) Long offset, @RequestParam(required = false) Long size, @RequestParam(required = false) PostOrderType postOrderType) {
        return this.postService.getPostsByBoardMetaCode(boardMetaCode, lastPostId, offset, size, postOrderType);
    }
}
