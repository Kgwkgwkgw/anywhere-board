package com.tommy.board.service.impl;

import com.tommy.board.domain.dto.*;
import com.tommy.board.domain.entity.BoardMeta;
import com.tommy.board.domain.type.BoardMetaType;
import com.tommy.board.domain.type.PostOrderType;
import com.tommy.board.domain.type.PreferenceDataType;
import com.tommy.board.domain.type.PreferenceType;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PostServiceImplTest {
    @Autowired
    private PostServiceImpl postService;
    @Autowired
    private BoardMetaServiceImpl boardMetaService;
    @Autowired
    private PreferenceServiceImpl preferenceService;

    private BoardMetaCreateRequest boardMetaCreateRequestCertificationType;
    private PostCreateRequest postCreateRequestCertification;

    @Before
    public void beforeAll() {
        boardMetaCreateRequestCertificationType = new BoardMetaCreateRequest();
        boardMetaCreateRequestCertificationType.setName("게시판 이름1");
        boardMetaCreateRequestCertificationType.setCode("toommmy");
        boardMetaCreateRequestCertificationType.setBoardMetaType(BoardMetaType.certification);
    }

    @Test
    public void 좋아요_순_게시글_조회() {
        BoardMeta boardMeta = boardMetaService.create(boardMetaCreateRequestCertificationType);
        List<PostCreateResponse> postCreateResponseList = new ArrayList<>();
        HashMap<Long, Long> postIdToLikedCount = new HashMap<>();

        Random random = new Random();
        // @Todo JPA는 Batch Insert가 없어서...
        //  개선하려면 JdbcTemplate을 사용해야할지도?
        for (int i = 0; i < 20; i++) {
            postCreateRequestCertification = new PostCreateRequest();
            postCreateRequestCertification.setTitle("제목1");
            postCreateRequestCertification.setContent("컨텐츠");
            postCreateRequestCertification.setBoardMetaCode(boardMeta.getCode());
            postCreateRequestCertification.setAccountId("account" + i);
            postCreateRequestCertification.setPassword("account" + i);
            postCreateResponseList.add(postService.create(postCreateRequestCertification));
        }
        for (int i = 0; i < 30; i++) {
            PreferenceCreateRequest preferenceCreateRequest = new PreferenceCreateRequest();
            preferenceCreateRequest.setDataType(PreferenceDataType.POST);

            int postIndexToBeLiked = random.nextInt(postCreateResponseList.size());
            PostCreateResponse postToBeLiked = postCreateResponseList.get(postIndexToBeLiked);
            Long currentLikedCount = postIdToLikedCount.get(postToBeLiked.getPostId());
            postIdToLikedCount.put(postToBeLiked.getPostId(), currentLikedCount == null ? 1L : currentLikedCount + 1);

            preferenceCreateRequest.setDataTypeId(postToBeLiked.getPostId());
            preferenceCreateRequest.setPreferenceType(PreferenceType.LIKE);
            preferenceCreateRequest.setAccountId("account" + i);
            preferenceCreateRequest.setBoardMetaCode(boardMeta.getCode());

            preferenceService.createPreference(preferenceCreateRequest);
        }

        List<PostForLikeOrder> postForLikeOrderList = new ArrayList<>();
        for (Long key : postIdToLikedCount.keySet()) {
            PostForLikeOrder postForLikeOrder = new PostForLikeOrder();
            postForLikeOrder.setPostId(key);
            postForLikeOrder.setLikeCount(postIdToLikedCount.get(key));
            postForLikeOrderList.add(postForLikeOrder);
        }
        postForLikeOrderList = postForLikeOrderList.stream().sorted(Comparator.comparingLong(PostForLikeOrder::getLikeCount)).collect(Collectors.toList());

        PostsReadResponse postsReadResponse = postService.getPostsByBoardMetaCode(boardMeta.getCode(), null, 0L, 10L, PostOrderType.LIKE);


        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(postForLikeOrderList.get(i).getPostId(), postsReadResponse.getPostReadResponseList().get(i).getPostId());
            Assert.assertEquals(postForLikeOrderList.get(i).getLikeCount(), postsReadResponse.getPostReadResponseList().get(i).getLikeCount());
        }

    }

    @Getter
    @Setter
    private static class PostForLikeOrder {
        private Long postId;
        private Long likeCount;
    }
}
