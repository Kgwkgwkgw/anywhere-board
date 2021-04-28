package com.tommy.board.domain.entity;

import com.tommy.board.domain.type.BoardMetaType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BoardMeta {
    @Column(name = "board_meta_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String code;
    private String name;

    @Enumerated(value = EnumType.STRING)
    private BoardMetaType type;

    @CreationTimestamp
    private Instant createdAt;
    @UpdateTimestamp
    private Instant modifiedAt;
}
