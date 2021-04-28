package com.tommy.board.service.impl;

import com.tommy.board.domain.dto.BoardMetaCreateRequest;
import com.tommy.board.domain.dto.BoardMetaUpdateRequest;
import com.tommy.board.domain.entity.BoardMeta;
import com.tommy.board.repository.BoardMetaQueryRepository;
import com.tommy.board.repository.BoardMetaRepository;
import com.tommy.board.service.BoardMetaService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
@Transactional
public class BoardMetaServiceImpl implements BoardMetaService {
    private final BoardMetaRepository boardMetaRepository;
    private final BoardMetaQueryRepository boardMetaQueryRepository;

    public BoardMetaServiceImpl(BoardMetaRepository boardMetaRepository, BoardMetaQueryRepository boardMetaQueryRepository) {
        this.boardMetaRepository = boardMetaRepository;
        this.boardMetaQueryRepository = boardMetaQueryRepository;
    }

    public BoardMeta create (BoardMetaCreateRequest boardMetaCreateRequest) {
        BoardMeta boardMeta = new BoardMeta();
        boardMeta.setType(boardMetaCreateRequest.getBoardMetaType());
        boardMeta.setName(boardMetaCreateRequest.getName());
        boardMeta.setCode(boardMetaCreateRequest.getCode());
        boardMeta.setCreatedAt(Instant.now());
        boardMeta.setModifiedAt(Instant.now());

        this.boardMetaRepository.save(boardMeta);
        return boardMeta;
    }
    public BoardMeta update (BoardMetaUpdateRequest boardMetaUpdateRequest) {
        BoardMeta boardMeta = this.boardMetaRepository.findById(boardMetaUpdateRequest.getId()).orElseThrow(IllegalArgumentException::new);
        boardMeta.setModifiedAt(Instant.now());
        boardMeta.setCreatedAt(Instant.now());
        boardMeta.setName(boardMetaUpdateRequest.getName());

        return boardMeta;
    }

    public BoardMeta findById (Long id) {
        return this.boardMetaRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }
    public BoardMeta findByCode (String code) {
        return this.boardMetaRepository.findByCode(code).orElseThrow(IllegalArgumentException::new);
    }
    public void remove (Long id) {
       this.boardMetaRepository.deleteById(id);
    }

}
