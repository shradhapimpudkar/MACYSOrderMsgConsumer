package com.order.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.order.dto.json.OrderMsgConsumerJson;
import com.order.dto.xml.FulfillmentOrder;

public interface MacyConsumerService {

	ResponseEntity<List<FulfillmentOrder>> consumeXmlMsg();

	ResponseEntity<List<OrderMsgConsumerJson>> consumeJsonMsg();

}
