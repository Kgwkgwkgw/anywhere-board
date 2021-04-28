package com.tommy.board.service.impl;

import com.querydsl.core.QueryResults;
import com.tommy.board.domain.dto.BoardMetaCreateRequest;
import com.tommy.board.domain.dto.BoardMetaUpdateRequest;
import com.tommy.board.domain.dto.PostCreateRequest;
import com.tommy.board.domain.entity.BoardMeta;
import com.tommy.board.domain.entity.Post;
import com.tommy.board.domain.entity.PostAnonym;
import com.tommy.board.domain.type.BoardMetaType;
import com.tommy.board.domain.type.PostOrderType;
import com.tommy.board.repository.BoardMetaRepository;
import com.tommy.board.repository.PostQueryRepository;
import com.tommy.board.repository.PostRepository;
import com.tommy.board.service.BoardMetaService;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Getter
@Setter
public class BoardMetaServieImplTest {
    @Autowired
    private BoardMetaServiceImpl boardMetaService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardMetaRepository boardMetaRepository;
    @Autowired
    private PostQueryRepository postQueryRepository;
    @Autowired
    private PostServiceImpl postService;

    @Test
    public void testList() {
        BoardMeta boardMeta = boardMetaRepository.findByCode("testa").orElseThrow(RuntimeException::new);
        QueryResults<? extends Post> results2 = postQueryRepository.findPostCertificationByBoardMetaId(boardMeta.getId(), null, null, 0L, 10L);
        results2.getResults();
    }
    @Test
    public void 리스트_조회() {
        // given
        BoardMetaCreateRequest boardMetaCreateRequest = new BoardMetaCreateRequest();
        boardMetaCreateRequest.setName("게시판 이름1");
        boardMetaCreateRequest.setCode("toommmy");
        boardMetaCreateRequest.setBoardMetaType(BoardMetaType.anonym);
        BoardMeta boardMeta = boardMetaService.create(boardMetaCreateRequest);

        PostCreateRequest postCreateRequest = new PostCreateRequest();
        postCreateRequest.setBoardMetaCode("toommmy");
        postCreateRequest.setTitle("제목11");
        postCreateRequest.setContent("내용111");
        postCreateRequest.setPassword("pasdqwdqw");
        postCreateRequest.setNickname("nickname1");

        PostCreateRequest postCreateRequest2= new PostCreateRequest();
        postCreateRequest.setBoardMetaCode("toommmy");
        postCreateRequest.setTitle("제목22");
        postCreateRequest.setContent("내용222");
        postCreateRequest.setPassword("pasdqwdqw");
        postCreateRequest.setNickname("nickname1");

        PostCreateRequest postCreateRequest3= new PostCreateRequest();
        postCreateRequest.setBoardMetaCode("toommmy");
        postCreateRequest.setTitle("제목33");
        postCreateRequest.setContent("내용333");
        postCreateRequest.setPassword("pasdqwdqw");
        postCreateRequest.setNickname("nickname1");

        postService.create(postCreateRequest);
        postService.create(postCreateRequest2);
        postService.create(postCreateRequest3);

        //when
//        postService.getPostsByBoardMetaCode(boardMeta.getCode(), 0, 10, null);


    }

    @Test
    public void test() {
        Post postAnonym = postQueryRepository.findPostByIdAndType(1L, BoardMetaType.anonym);

//        BoardMeta boardMeta = new BoardMeta();
//        boardMeta.setName("dddd");
//        boardMeta.setType(BoardMetaType.anonym);
//        this.boardMetaRepository.save(boardMeta);
//
//        PostAnonym postAnonym = new PostAnonym();
//        postAnonym.setPassword("test");
//        postAnonym.setBoardMeta(boardMeta);
//        postAnonym.setNickname("nickname");
//        postAnonym.setTitle("타이틀");
//        postAnonym.setContent("제목");
//        this.postRepository.save(postAnonym);
//

    }

    @Test(expected = IllegalArgumentException.class)
    public void 잘못된_게시판_식별자() {
        // given
        BoardMetaUpdateRequest boardMetaUpdateRequest = new BoardMetaUpdateRequest();
        boardMetaUpdateRequest.setId(101231L);
        boardMetaUpdateRequest.setName("테스트22");
        // when
        boardMetaService.update(boardMetaUpdateRequest);

        // then
        fail("예외 미 호출");
    }

    @Test
    public void CODE로_조회() {
        // given
        BoardMetaCreateRequest boardMetaCreateRequest = new BoardMetaCreateRequest();
        boardMetaCreateRequest.setName("게시판 이름1");
        boardMetaCreateRequest.setCode("toommmy");
        boardMetaCreateRequest.setBoardMetaType(BoardMetaType.anonym);

        //when
        BoardMeta boardMeta1 = boardMetaService.create(boardMetaCreateRequest);
        BoardMeta boardMeta2 = boardMetaService.findByCode(boardMetaCreateRequest.getCode());

        // then
        assertThat(boardMeta1.getId()).isEqualTo(boardMeta2.getId());


    }
    @Test(expected = IllegalArgumentException.class)
    public void CODE_NULL_값_조회() {
        // given
        BoardMeta boardMeta2 = boardMetaService.findByCode(null);
    }
}
