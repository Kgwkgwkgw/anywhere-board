package com.tommy.board.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tommy.board.domain.dto.PreferenceCount;
import com.tommy.board.domain.entity.Preference;
import com.tommy.board.domain.entity.QPreference;
import com.tommy.board.domain.type.PreferenceDataType;
import com.tommy.board.domain.type.PreferenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PreferenceQueryRepository {
    private JPAQueryFactory jpaQueryFactory;
    @Autowired
    public PreferenceQueryRepository (JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }
    public List<PreferenceCount> findPreferenceByDataTypeAndDataTypeId (PreferenceDataType preferenceDataType, List<Long> dataTypeIdList) {
        QPreference preference = QPreference.preference;
        NumberExpression<Integer> likeCount = new CaseBuilder()
                .when(preference.preferenceType.eq(PreferenceType.LIKE))
                .then(1)
                .otherwise(0)
                .as("likeCount");

        NumberExpression<Integer> dislikeCount = new CaseBuilder()
                .when(preference.preferenceType.eq(PreferenceType.DISLIKE))
                .then(1)
                .otherwise(0)
                .as("dislikeCount");
        return jpaQueryFactory.select(Projections.bean(PreferenceCount.class,
                likeCount, dislikeCount, preference.preferenceDataType, preference.dataTypeId))
                .from(preference)
                .where(preference.preferenceDataType.eq(preferenceDataType)
                        .and(preference.dataTypeId.in(dataTypeIdList)))
                .groupBy(preference.dataTypeId, preference.preferenceDataType)
                .orderBy(likeCount.desc()).fetch();
    }
    public QueryResults<PreferenceCount> findPreferencesByBoardMetaIdByOffsetAndLimit (Long boardMetaId, Long offset, Long limit) {
        QPreference preference = QPreference.preference;
        NumberExpression<Long> likeCount = new CaseBuilder()
                .when(preference.preferenceType.eq(PreferenceType.LIKE))
                .then(1L)
                .otherwise(0L)
                .as("likeCount");
        // alias로 order by 하기!
//        NumberPath<Long> likeCountAlias = Expressions.numberPath(Long.class, "likeCount");
        // @TODO 복잡한 쿼리에서는 fetchResult가 잘 동작하지 않는다.
        // count 쿼리를 별도로 만들어야 한다.

        NumberExpression<Long> dislikeCount = new CaseBuilder()
                .when(preference.preferenceType.eq(PreferenceType.DISLIKE))
                .then(1L)
                .otherwise(0L)
                .as("dislikeCount");
        return jpaQueryFactory.select(Projections.bean(PreferenceCount.class, likeCount, dislikeCount, preference.preferenceDataType, preference.dataTypeId))
                .from(preference)
                .where(preference.boardMeta.id.eq(boardMetaId).and(preference.preferenceDataType.eq(PreferenceDataType.POST)))
//                .groupBy(preference.preferenceDataType)
                .groupBy(preference.dataTypeId, preference.preferenceDataType)
//                .orderBy(likeCountAlias.desc())
                .offset(offset)
                .limit(limit)
                .fetchResults();

    }
}
