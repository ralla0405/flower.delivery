package com.kirinit.service.flower.delivery.repository;

import com.kirinit.service.flower.delivery.entity.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryCustomRepository {

    private final EntityManager em;

    @Override
    public List<Delivery> findAll(DeliverySearch deliverySearch) {
        JPAQueryFactory query = new JPAQueryFactory(em);
        QDelivery delivery = QDelivery.delivery;
        String start = String.valueOf(deliverySearch.getStartDate());
        String end = String.valueOf(deliverySearch.getEndDate());

        return query.selectFrom(delivery)
                .where(statusEq(deliverySearch.getDeliveryStatus()), getBetween(delivery, start, end))
                .orderBy(delivery.date.desc())
                .fetch();
    }

    private BooleanExpression getBetween(QDelivery delivery, String start, String end) {
        if (start.equals("null")) {
            String todayStart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String todayEnd = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return delivery.date.between(todayStart, todayEnd);
        }
        return delivery.date.between(start, end);
    }

    private BooleanExpression statusEq(DeliveryStatus statusCond) {
        if (statusCond == null) {
            return null;
        }
        return QDelivery.delivery.status.eq(statusCond);
    }
}