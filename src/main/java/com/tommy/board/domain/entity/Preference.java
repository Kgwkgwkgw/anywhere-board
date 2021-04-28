package com.tommy.board.domain.entity;

import com.tommy.board.domain.type.PreferenceDataType;
import com.tommy.board.domain.type.PreferenceType;
import lombok.*;

import javax.jdo.annotations.Join;
import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Preference {
    @Id
    @Column(name="preference_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PreferenceDataType preferenceDataType;
    private Long dataTypeId;

    @ManyToOne
    @JoinColumn(name = "board_meta_id")
    private BoardMeta boardMeta;
    private String accountId;
    @Enumerated(EnumType.STRING)
    private PreferenceType preferenceType;
}
