package com.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.entity.xml.FulfillmentOrderEntity;

public interface ConsumerMsgXmlRepo extends JpaRepository<FulfillmentOrderEntity, Integer> {
}