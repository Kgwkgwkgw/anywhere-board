package com.tommy.board.domain.dto;

import com.tommy.board.domain.type.PreferenceDataType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PreferenceCount {
    PreferenceDataType preferenceDataType;
    Long dataTypeId;
    Long likeCount;
    Long disLikeCount;
}
