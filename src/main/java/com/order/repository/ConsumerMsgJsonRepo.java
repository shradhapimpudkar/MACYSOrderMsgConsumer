package com.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.entity.json.OrderMsgConsumerJsonEntity;

public interface ConsumerMsgJsonRepo extends JpaRepository<OrderMsgConsumerJsonEntity, Integer> {
}