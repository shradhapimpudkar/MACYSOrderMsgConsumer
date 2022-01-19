package com.order.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.dto.json.OrderMsgConsumerJson;
import com.order.dto.xml.FulfillmentOrder;
import com.order.service.MacyConsumerService;

@RestController
@RequestMapping("/orderConsumer")
public class MacyConsumerController {

	@Autowired
	MacyConsumerService macyConsumerService;

	// #Query to Consume/extract XML msg to rabbit queue and save in sqldb
	@GetMapping(value = "/xml", produces = { MediaType.APPLICATION_XML_VALUE })
	ResponseEntity<List<FulfillmentOrder>> getXmlMessage() {
		return macyConsumerService.consumeXmlMsg();
	}

	// #Query to Consume/extract Json msg from rabbit queue and save in sqldb
	@GetMapping(value = "/json", produces = { MediaType.APPLICATION_JSON_VALUE })
	ResponseEntity<List<OrderMsgConsumerJson>> getJsonMessage() {
		return macyConsumerService.consumeJsonMsg();
	}
}