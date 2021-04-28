package com.tommy.board.domain.search;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;

@Document(indexName = "post_annonym", createIndex = false)
@Getter
@Setter
public class PostAnonym {
    private Long id;
    @Field(type = FieldType.Text, name = "title")
    private String title;
    @Field(type = FieldType.Text, name = "content")
    private String content;
    @MultiField(mainField = @Field(type = FieldType.Text), otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword))
    private String nickname;
    private Instant createdAt;
    private Instant modifiedAt;
}
