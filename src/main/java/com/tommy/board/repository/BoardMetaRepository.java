package com.tommy.board.repository;

import com.tommy.board.domain.entity.BoardMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardMetaRepository extends JpaRepository<BoardMeta, Long> {
    public Optional<BoardMeta> findByCode(String code);
}
