package com.tommy.board.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tommy.board.domain.entity.*;
import com.tommy.board.domain.type.BoardMetaType;
import com.tommy.board.domain.type.PostOrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostQueryRepository {
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    public PostQueryRepository(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Post findPostByIdAndType(Long id, BoardMetaType boardMetaType) {
        QPostAnonym qPostAnonym = QPostAnonym.postAnonym;
        QPostCertification qPostCertification = QPostCertification.postCertification;

        if (boardMetaType == BoardMetaType.anonym) {
            return this.jpaQueryFactory.select(qPostAnonym)
                    .from(qPostAnonym)
                    .where(qPostAnonym.id.eq(id)).fetchOne();
        } else {
            return this.jpaQueryFactory.select(qPostCertification)
                    .from(qPostCertification)
                    .where(qPostCertification.id.eq(id)).fetchOne();
        }
    }
    public List<? extends Post> findPostsByIdListAndType(List<Long> idList, BoardMetaType boardMetaType) {
        QPostAnonym qPostAnonym = QPostAnonym.postAnonym;
        QPostCertification qPostCertification = QPostCertification.postCertification;

        if (boardMetaType == BoardMetaType.anonym) {
            return this.jpaQueryFactory.select(qPostAnonym)
                    .from(qPostAnonym)
                    .where(qPostAnonym.id.in(idList)).fetch();
        } else {
            return this.jpaQueryFactory.select(qPostCertification)
                    .from(qPostCertification)
                    .where(qPostCertification.id.in(idList)).fetch();
        }
    }

    public QueryResults<PostAnonym> findPostAnonymByBoardMetaId(Long boardMetaId, Long lastPostId, PostOrderType orderType, Long offset, Long size) {
        QPostAnonym qPostAnonym = QPostAnonym.postAnonym;
        QBoardMeta qBoardMeta = QBoardMeta.boardMeta;

        if (offset == null) {
            offset = 0L;
        }
        if (size == null || size <= 0 || size > 100) {
            size = 10L;
        }
        if (lastPostId == null || lastPostId < 0L) {
            lastPostId = 0L;
        }
        if (orderType == null) {
            orderType = PostOrderType.RECENT;
        }

        JPAQuery<PostAnonym> query = null;
        query = this.jpaQueryFactory.select(qPostAnonym)
                .from(qPostAnonym)
                .innerJoin(qPostAnonym._super.boardMeta, qBoardMeta)
                .fetchJoin()
                .where(qPostAnonym._super.boardMeta.id.eq(boardMetaId).and(qPostAnonym.id.gt(lastPostId)));
        if (orderType == PostOrderType.RECENT) {
            query.orderBy(qPostAnonym.createdAt.desc());
        }
        query
                .offset(offset)
                .limit(size);
        return query.fetchResults();
    }
    public QueryResults<PostCertification> findPostCertificationByBoardMetaId(Long boardMetaId, Long lastPostId, PostOrderType orderType, Long offset, Long size) {
        QPostCertification qPostCertification = QPostCertification.postCertification;
        QBoardMeta qBoardMeta = QBoardMeta.boardMeta;

        if (offset == null) {
            offset = 0L;
        }
        if (size == null || size <= 0 || size > 30) {
            size = 10L;
        }
        if (lastPostId == null || lastPostId < 0L) {
            lastPostId = 0L;
        }
        if (orderType == null) {
            orderType = PostOrderType.RECENT;
        }

        JPAQuery<PostCertification> query = this.jpaQueryFactory.select(qPostCertification)
                .from(qPostCertification)
                .innerJoin(qPostCertification._super.boardMeta, qBoardMeta)
                .fetchJoin()
                .where(qPostCertification.id.gt(lastPostId).and(qPostCertification._super.boardMeta.id.eq(boardMetaId)));

        if (orderType == PostOrderType.RECENT) {
            query.orderBy(qPostCertification.createdAt.desc());
        }
        query
                .offset(offset)
                .limit(size);
        return query.fetchResults();
    }
}
