package com.tommy.board.repository;

import com.tommy.board.domain.entity.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
}
