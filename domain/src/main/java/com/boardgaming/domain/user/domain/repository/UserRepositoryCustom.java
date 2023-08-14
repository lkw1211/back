package com.boardgaming.domain.user.domain.repository;

import com.boardgaming.domain.user.domain.QUser;
import com.boardgaming.domain.user.dto.response.UserResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private static final QUser qUser = QUser.user;

    public UserResponse getUserInfo(final String email) {
        return queryFactory.select(
            Projections.constructor(
                UserResponse.class,
                qUser.id,
                qUser.email,
                qUser.name,
                qUser.role,
                qUser.imageFileUrl
            ))
            .from(qUser)
            .where(qUser.email.eq(email))
            .fetchOne();
    }

    public UserResponse findById(final String userId) {
        return queryFactory.select(
                Projections.constructor(
                    UserResponse.class,
                    qUser.id,
                    qUser.email,
                    qUser.name,
                    qUser.role,
                    qUser.imageFileUrl
                ))
            .from(qUser)
            .where(qUser.id.eq(userId))
            .fetchOne();
    }
}
