package com.tommy.board.service.impl;

import com.querydsl.core.QueryResults;
import com.tommy.board.domain.dto.*;
import com.tommy.board.domain.entity.BoardMeta;
import com.tommy.board.domain.entity.Post;
import com.tommy.board.domain.entity.PostAnonym;
import com.tommy.board.domain.entity.PostCertification;
import com.tommy.board.domain.type.BoardMetaType;
import com.tommy.board.domain.type.PostOrderType;
import com.tommy.board.domain.type.PreferenceDataType;
import com.tommy.board.repository.PostQueryRepository;
import com.tommy.board.repository.PostRepository;
import com.tommy.board.repository.PreferenceQueryRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostServiceImpl {
    private BoardMetaServiceImpl boardMetaService;
    private PostRepository postRepository;
    private PostQueryRepository postQueryRepository;
    private PasswordEncoder passwordEncoder;
    private PreferenceQueryRepository preferenceQueryRepository;
    private Long POSTS_ORDER_BY_LIKE_MAX_SIZE = 100L;

    public PostServiceImpl(BoardMetaServiceImpl boardMetaService, PostRepository postRepository, PostQueryRepository postQueryRepository, PreferenceQueryRepository preferenceQueryRepository, PasswordEncoder passwordEncoder) {
        this.boardMetaService = boardMetaService;
        this.postRepository = postRepository;
        this.postQueryRepository = postQueryRepository;
        this.preferenceQueryRepository = preferenceQueryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PostCreateResponse create(PostCreateRequest postCreateRequest) {
        String boardMetaCode = postCreateRequest.getBoardMetaCode();
        // Todo 캐싱 적용
        BoardMeta boardMeta = this.boardMetaService.findByCode(boardMetaCode);

        if (boardMeta.getType().equals(BoardMetaType.anonym)) {
            PostAnonym postAnonym = new PostAnonym();
            postAnonym.setBoardMeta(boardMeta);
            postAnonym.setTitle(postCreateRequest.getTitle());
            postAnonym.setContent(postCreateRequest.getContent());
            postAnonym.setNickname(postCreateRequest.getNickname());

            postAnonym.setCreatedAt(Instant.now());
            postAnonym.setModifiedAt(Instant.now());
            postAnonym.setHashedPassword(passwordEncoder.encode(postCreateRequest.getPassword()));
            this.postRepository.save(postAnonym);
            return PostCreateResponse.builder().postId(postAnonym.getId()).build();
        } else {
            PostCertification postCertification = new PostCertification();
            postCertification.setBoardMeta(boardMeta);
            postCertification.setTitle(postCreateRequest.getTitle());
            postCertification.setContent(postCreateRequest.getContent());
            postCertification.setNickname(postCreateRequest.getNickname());
            postCertification.setAccountId(postCreateRequest.getAccountId());

            postCertification.setCreatedAt(Instant.now());
            postCertification.setModifiedAt(Instant.now());
            this.postRepository.save(postCertification);
            return PostCreateResponse.builder().postId(postCertification.getId()).build();
        }

        // Todo 엘라스틱 서치에 저장 혹은 큐에 전송
        // Todo 재처리 가능하도록 DB에 저장

    }

    public PostReadResponse getPostByIdAndBoardMetaCode(Long postId, String boardMetaCode) {
        // Todo 캐싱 적용
        BoardMeta boardMeta = this.boardMetaService.findByCode(boardMetaCode);
        // findById로 조회하면, Outer Join이 2번 만들어져서, 한번만 조인 가능하도록 QueryDsl을 사용

        // Todo 캐싱 적용
        Post post = postQueryRepository.findPostByIdAndType(postId, boardMeta.getType());

        PostReadResponse postReadResponse = new PostReadResponse();
        postReadResponse.setBoardMetaCode(boardMetaCode);
        postReadResponse.setContent(post.getContent());
        postReadResponse.setNickname(post.getNickname());
        postReadResponse.setTitle(post.getTitle());
        postReadResponse.setPostId(postId);


        if (post instanceof PostCertification) {
            PostCertification postCertification = (PostCertification) post;
            postReadResponse.setAccountId(postCertification.getAccountId());
            ArrayList<Long> idList = new ArrayList<>();
            idList.add(post.getId());
            List<PreferenceCount> preferenceCountList = preferenceQueryRepository.findPreferenceByDataTypeAndDataTypeId(PreferenceDataType.POST, idList);
            postReadResponse.setLikeCount(preferenceCountList.get(0).getLikeCount());
            postReadResponse.setDisLikeCount(preferenceCountList.get(0).getDisLikeCount());
        }

        return postReadResponse;
    }

    // list 구현
    // controller 에서 잘되는지 한번 테스트
    public PostsReadResponse getPostsByBoardMetaCode(String boardMetaCode, Long lastPostId, Long offset, Long size, PostOrderType postOrderType) {
        if (lastPostId == null && offset > 0) {
            throw new IllegalArgumentException();
        }
        PostsReadResponse postsReadResponse = new PostsReadResponse();
        // Todo 캐싱 적용
        BoardMeta boardMeta = this.boardMetaService.findByCode(boardMetaCode);

        if (postOrderType == null) {
            postOrderType = PostOrderType.RECENT;
        }
        if (postOrderType == PostOrderType.LIKE) {
            if (offset > POSTS_ORDER_BY_LIKE_MAX_SIZE) {
                throw new IllegalArgumentException();
            }
            QueryResults<PreferenceCount> queryResults = preferenceQueryRepository.findPreferencesByBoardMetaIdByOffsetAndLimit(boardMeta.getId(), offset, size);
            List<Long> postIdList = queryResults.getResults().stream().map(PreferenceCount::getDataTypeId).collect(Collectors.toUnmodifiableList());
            // 인증된 사용자 게시판 타입만 좋아요 기능을 사용할 수 있다.
            List<PostCertification> posts = (List<PostCertification>) postQueryRepository.findPostsByIdListAndType(postIdList, boardMeta.getType());

            System.out.println("print ! : " + queryResults.getResults());

            Map<Long, PreferenceCount> postIdToPreferenceCount = queryResults.getResults().stream().collect(Collectors.toUnmodifiableMap(PreferenceCount::getDataTypeId, preferenceCount -> preferenceCount));

            List<PostReadResponse> postReadResponseList = posts.stream().map(postCertification -> {
                PostReadResponse postReadResponse = new PostReadResponse();
                postReadResponse.setPostId(postCertification.getId());
                postReadResponse.setTitle(postCertification.getTitle());
                postReadResponse.setNickname(postCertification.getNickname());
                postReadResponse.setContent(postCertification.getContent());
                postReadResponse.setBoardMetaCode(postCertification.getBoardMeta().getName());
                postReadResponse.setAccountId(postCertification.getAccountId());

                // 좋아요/싫어요 설정
                PreferenceCount currentPreferenceCount = postIdToPreferenceCount.get(postCertification.getId());
                postReadResponse.setLikeCount(currentPreferenceCount.getLikeCount());
                postReadResponse.setDisLikeCount(currentPreferenceCount.getDisLikeCount());
                return postReadResponse;
            }).collect(Collectors.toList());

            postsReadResponse.setPostReadResponseList(postReadResponseList);
            postsReadResponse.setTotal(queryResults.getTotal());
            postsReadResponse.setOffset(queryResults.getOffset());
            postsReadResponse.setLimit(queryResults.getLimit());
            return postsReadResponse;

        // 최신 순
        } else {
            if (boardMeta.getType() == BoardMetaType.anonym) {
                QueryResults<PostAnonym> queryResults = postQueryRepository.findPostAnonymByBoardMetaId(boardMeta.getId(), lastPostId, postOrderType, offset, size);
                List<PostReadResponse> postReadResponseList = queryResults.getResults().stream().map(postAnonym -> {
                    PostReadResponse postReadResponse = new PostReadResponse();
                    postReadResponse.setPostId(postAnonym.getId());
                    postReadResponse.setTitle(postAnonym.getTitle());
                    postReadResponse.setNickname(postAnonym.getNickname());
                    postReadResponse.setContent(postAnonym.getContent());
                    postReadResponse.setBoardMetaCode(postAnonym.getBoardMeta().getName());
                    return postReadResponse;
                }).collect(Collectors.toList());
                postsReadResponse.setPostReadResponseList(postReadResponseList);
                postsReadResponse.setTotal(queryResults.getTotal());
                postsReadResponse.setOffset(queryResults.getOffset());
                postsReadResponse.setLimit(queryResults.getLimit());
                return postsReadResponse;

            } else {
                QueryResults<PostCertification> queryResults = postQueryRepository.findPostCertificationByBoardMetaId(boardMeta.getId(), lastPostId, postOrderType, offset, size);

                List<Long> postIdList = queryResults.getResults().stream().map(Post::getId).collect(Collectors.toUnmodifiableList());
                List<PreferenceCount> preferenceCountList = preferenceQueryRepository.findPreferenceByDataTypeAndDataTypeId(PreferenceDataType.POST, postIdList);
                Map<Long, PreferenceCount> postIdToPreferenceCount = preferenceCountList.stream().collect(Collectors.toUnmodifiableMap(PreferenceCount::getDataTypeId, preferenceCount -> preferenceCount));

                List<PostReadResponse> postReadResponseList = queryResults.getResults().stream().map(postCertification -> {
                    PostReadResponse postReadResponse = new PostReadResponse();
                    postReadResponse.setPostId(postCertification.getId());
                    postReadResponse.setTitle(postCertification.getTitle());
                    postReadResponse.setNickname(postCertification.getNickname());
                    postReadResponse.setContent(postCertification.getContent());
                    postReadResponse.setBoardMetaCode(postCertification.getBoardMeta().getName());
                    postReadResponse.setAccountId(postCertification.getAccountId());

                    // 좋아요/싫어요 설정
                    PreferenceCount currentPreferenceCount = postIdToPreferenceCount.get(postCertification.getId());
                    postReadResponse.setLikeCount(currentPreferenceCount.getLikeCount());
                    postReadResponse.setDisLikeCount(currentPreferenceCount.getDisLikeCount());
                    return postReadResponse;
                }).collect(Collectors.toList());
                postsReadResponse.setPostReadResponseList(postReadResponseList);
                postsReadResponse.setTotal(queryResults.getTotal());
                postsReadResponse.setOffset(queryResults.getOffset());
                postsReadResponse.setLimit(queryResults.getLimit());
                return postsReadResponse;
            }
        }
    }
}
