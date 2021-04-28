package com.tommy.board.controller;

import com.tommy.board.domain.entity.BoardMeta;
import com.tommy.board.domain.entity.PostAnonym;
import com.tommy.board.domain.entity.PostCertification;
import com.tommy.board.domain.entity.Preference;
import com.tommy.board.domain.type.BoardMetaType;
import com.tommy.board.domain.type.PreferenceDataType;
import com.tommy.board.domain.type.PreferenceType;
import com.tommy.board.repository.BoardMetaRepository;
import com.tommy.board.repository.PostRepository;
import com.tommy.board.repository.PreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardMetaController {
    private PostRepository postRepository;
    // @Todo 제거할 코드임 (테스트용)
    private BoardMetaRepository boardMetaRepository;
    private PreferenceRepository preferenceRepository;
    @Autowired
    public BoardMetaController(PostRepository postRepository, BoardMetaRepository boardMetaRepository, PreferenceRepository preferenceRepository) {
        this.postRepository = postRepository;
        this.boardMetaRepository = boardMetaRepository;
        this.preferenceRepository = preferenceRepository;

        // @Todo 제거할 코드임 (테스트용)
        BoardMeta boardMeta = new BoardMeta();
        try {
            boardMeta.setCode("dddddd");
            boardMeta.setName("dddd");
            boardMeta.setType(BoardMetaType.anonym);
            this.boardMetaRepository.save(boardMeta);
        } catch (DataIntegrityViolationException e) {
            // @Todo 중복 삽입 처리
        }

        PostAnonym postAnonym = new PostAnonym();
        postAnonym.setHashedPassword("test");
        postAnonym.setBoardMeta(boardMeta);
        postAnonym.setNickname("nickname");
        postAnonym.setTitle("타이틀");
        postAnonym.setContent("제목");


        BoardMeta boardMeta2 = new BoardMeta();
        try {
            boardMeta2.setCode("testa");
            boardMeta2.setName("dddd");
            boardMeta2.setType(BoardMetaType.certification);
            this.boardMetaRepository.save(boardMeta2);
        } catch (DataIntegrityViolationException e) {
        }
        PostCertification postCertification = new PostCertification();
        postCertification.setAccountId("qwdqwdqw");
        postCertification.setBoardMeta(boardMeta2);
        postCertification.setTitle("제목...");
        postCertification.setContent("내용...");

        Preference preference = new Preference();
        preference.setPreferenceDataType(PreferenceDataType.POST);
        preference.setPreferenceType(PreferenceType.LIKE);
        preference.setAccountId(postCertification.getAccountId());


        this.postRepository.save(postAnonym);
        this.postRepository.save(postCertification);
        this.preferenceRepository.save(preference);

        this.postRepository = postRepository;
        this.boardMetaRepository = boardMetaRepository;
    }
    @GetMapping("/")
    public BoardMeta boardMetaPage () {
        return this.boardMetaRepository.findByCode("dddddd").orElseThrow(RuntimeException::new);
    }
}
