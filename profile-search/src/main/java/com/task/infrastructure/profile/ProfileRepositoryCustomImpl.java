package com.task.infrastructure.profile;

import static com.task.infrastructure.profile.QProfileEntity.profileEntity;
import static com.task.infrastructure.profile_stat.QProfileViewStatEntity.*;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.PageResult;
import com.task.controller.response.ProfileResponse;
import com.task.util.DateUtils;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepositoryCustomImpl implements ProfileRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProfileRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<ProfileResponse> searchById(Long id) {
        JPQLQuery<Long> viewCount = JPAExpressions
            .select(profileViewStatEntity.id.count().coalesce(0L))
            .from(profileViewStatEntity)
            .where(profileViewStatEntity.profileEntity.id.eq(id));

        ProfileResponse response =  queryFactory
            .select(Projections.constructor(ProfileResponse.class,
                profileEntity.id,
                profileEntity.name,
                profileEntity.createdAt,
                viewCount
            ))
            .from(profileEntity)
            .where(profileEntity.id.eq(id))
            .fetchOne();
        return Optional.ofNullable(response);
    }

    @Override
    public PageResult<ProfileResponse> getAllByCondition(Pageable pageable) {
        JPQLQuery<Long> viewCountSubQuery = getViewCountSubQuery();

        Long totalCount = queryFactory
            .select(profileEntity.id.count().coalesce(0L))
            .from(profileEntity)
            .fetchOne();

        List<ProfileResponse> data = queryFactory
            .select(Projections.constructor(ProfileResponse.class,
                profileEntity.id,
                profileEntity.name,
                profileEntity.createdAt,
                viewCountSubQuery
            ))
            .from(profileEntity)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderCondition(pageable))
            .fetch();

        return new PageResult<>(Long.valueOf(pageable.getOffset()).intValue(),
            pageable.getPageSize(), totalCount, data);
    }

    private JPQLQuery<Long> getViewCountSubQuery(){
        return JPAExpressions
            .select(profileViewStatEntity.id.count().coalesce(0L))
            .from(profileViewStatEntity)
            .where(profileViewStatEntity.profileEntity.id.eq(profileEntity.id));
    }


    private OrderSpecifier[] getOrderCondition(Pageable pageable) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            switch (order.getProperty()) {
                case "name":
                    orderSpecifiers.add(new OrderSpecifier(direction, profileEntity.name));
                    break;
                case "viewCount":
                    orderSpecifiers.add(new OrderSpecifier(direction, getViewCountSubQuery()));
                    break;
                case "createdAt":
                    orderSpecifiers.add(new OrderSpecifier(direction, profileEntity.createdAt));
                    break;
                default:
                    break;
            }
        }

        return orderSpecifiers.toArray(new OrderSpecifier[orderSpecifiers.size()]);
    }

}
