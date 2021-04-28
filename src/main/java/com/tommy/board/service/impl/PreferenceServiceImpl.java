package com.tommy.board.service.impl;

import com.tommy.board.domain.dto.PreferenceCreateRequest;
import com.tommy.board.domain.dto.PreferenceCreateResponse;
import com.tommy.board.domain.entity.BoardMeta;
import com.tommy.board.domain.entity.Preference;
import com.tommy.board.domain.type.PreferenceDataType;
import com.tommy.board.repository.BoardMetaRepository;
import com.tommy.board.repository.PreferenceRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class PreferenceServiceImpl {
    private PreferenceRepository preferenceRepository;
    private BoardMetaRepository boardMetaRepository;
    public PreferenceServiceImpl (PreferenceRepository preferenceRepository, BoardMetaRepository boardMetaRepository) {
        this.preferenceRepository = preferenceRepository;
        this.boardMetaRepository = boardMetaRepository;
    }
    public PreferenceCreateResponse createPreference (PreferenceCreateRequest preferenceCreateRequest) {
        if (preferenceCreateRequest.getAccountId() == null || preferenceCreateRequest.getDataTypeId() == null) {
           throw new IllegalArgumentException();
        }
        Preference preference = new Preference();
        if (preferenceCreateRequest.getDataType() == PreferenceDataType.POST) {
            BoardMeta boardMeta = this.boardMetaRepository.findByCode(preferenceCreateRequest.getBoardMetaCode()).orElseThrow(IllegalAccessError::new);
            preference.setBoardMeta(boardMeta);
        }
        preference.setPreferenceType(preferenceCreateRequest.getPreferenceType());
        preference.setPreferenceDataType(preferenceCreateRequest.getDataType());
        preference.setAccountId(preference.getAccountId());
        preference.setDataTypeId(preferenceCreateRequest.getDataTypeId());

        preferenceRepository.save(preference);
        return PreferenceCreateResponse.builder().preferenceId(preference.getId()).build();
    }
}
