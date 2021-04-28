package com.tommy.board.domain.dto;

import com.tommy.board.domain.type.PreferenceDataType;
import com.tommy.board.domain.type.PreferenceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferenceCreateRequest {
    private PreferenceDataType dataType;
    private Long dataTypeId;
    // 게시글 타입(post)일 때만 채워지는 데이터
    private String boardMetaCode;
    private String accountId;
    private PreferenceType preferenceType;
}
