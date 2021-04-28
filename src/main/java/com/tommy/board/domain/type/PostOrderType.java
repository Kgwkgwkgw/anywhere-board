package com.tommy.board.domain.type;

import org.hibernate.cache.spi.entry.ReferenceCacheEntryImpl;

public enum PostOrderType {
    RECENT("최신 순"),
    LIKE("좋아요 순");


    private String desc;
    PostOrderType (String desc) {
        this.desc = desc;
    }

}
