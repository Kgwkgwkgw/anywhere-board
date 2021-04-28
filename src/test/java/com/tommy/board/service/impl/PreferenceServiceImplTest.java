package com.tommy.board.service.impl;

import com.tommy.board.domain.dto.*;
import com.tommy.board.domain.entity.BoardMeta;
import com.tommy.board.domain.entity.Preference;
import com.tommy.board.domain.type.BoardMetaType;
import com.tommy.board.domain.type.PreferenceDataType;
import com.tommy.board.domain.type.PreferenceType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PreferenceServiceImplTest {
    @Autowired
    PreferenceServiceImpl preferenceService;

    @Autowired
    BoardMetaServiceImpl boardMetaService;

    @Autowired
    PostServiceImpl postService;


    BoardMetaCreateRequest boardMetaCreateRequest;
    PreferenceCreateRequest preferenceCreateRequest;
    PostCreateRequest postCreateRequest;

    @Before
    public void before() {
        boardMetaCreateRequest = new BoardMetaCreateRequest();
        boardMetaCreateRequest.setName("게시판 이름1");
        boardMetaCreateRequest.setCode("toommmy");
        boardMetaCreateRequest.setBoardMetaType(BoardMetaType.certification);

        postCreateRequest = new PostCreateRequest();
        postCreateRequest.setBoardMetaCode(boardMetaCreateRequest.getCode());
        postCreateRequest.setAccountId("writer");
        postCreateRequest.setTitle("title");
        postCreateRequest.setContent("content");
        postCreateRequest.setNickname("tommy...");

        preferenceCreateRequest = new PreferenceCreateRequest();
        preferenceCreateRequest.setBoardMetaCode(boardMetaCreateRequest.getCode());
        preferenceCreateRequest.setAccountId("liker");
        preferenceCreateRequest.setDataType(PreferenceDataType.POST);
        preferenceCreateRequest.setPreferenceType(PreferenceType.LIKE);
        preferenceCreateRequest.setDataTypeId(1L);
    }
    @Test
    public void 게시판_선호도_참여 () {
        boardMetaService.create(boardMetaCreateRequest);
        PostCreateResponse postCreateResponse = postService.create(postCreateRequest);
        preferenceCreateRequest.setDataTypeId(postCreateResponse.getPostId());
        PreferenceCreateResponse preferenceCreateResponse = preferenceService.createPreference(preferenceCreateRequest);
        Assert.notNull(preferenceCreateResponse.getPreferenceId(), "선호도 테이블 id가 null입니다.");
    }
}
