package com.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.order.config.ConsumerMsgConfig;
import com.order.dto.json.OrderMsgConsumerJson;
import com.order.dto.xml.FulfillmentOrder;
import com.order.entity.json.OrderMsgConsumerJsonEntity;
import com.order.entity.xml.FulfillmentOrderEntity;
import com.order.repository.ConsumerMsgJsonRepo;
import com.order.repository.ConsumerMsgXmlRepo;

@Service
public class MacyConsumerrServiceImpl implements MacyConsumerService {

	@Autowired
	ConsumerMsgXmlRepo consumerMsgXmlRepo;

	@Autowired
	ConsumerMsgJsonRepo consumerMsgJsonRepo;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	@Qualifier("CONSUMER_RabbitAdmin")
	RabbitAdmin rabbitAdmin;

	@Autowired
	@Qualifier("CONSUMER_XMLAmqpTemplate")
	AmqpTemplate xmlAmqpTemplate;

	@Autowired
	@Qualifier("CONSUMER_JsonAmqpTemplate")
	AmqpTemplate jsonAmqpTemplate;


	// #Query to Consume Xml msg to rabbit queue
	@Override
	public ResponseEntity<List<FulfillmentOrder>> consumeXmlMsg() {
		List<FulfillmentOrder> list = new ArrayList<>();
		Properties p = rabbitAdmin.getQueueProperties(ConsumerMsgConfig.XML_Queue_Key);
		int queueCount = (Integer) (p != null ? p.get(RabbitAdmin.QUEUE_MESSAGE_COUNT) : 0);
		for (int i = 0; i < queueCount; i++) {
			try {
			//	String s=new String((byte[]) xmlAmqpTemplate.receiveAndConvert());
				String msg = (String) xmlAmqpTemplate.receiveAndConvert();
				FulfillmentOrder xmlOrder = new XmlMapper()
						.readValue(new String((byte[]) xmlAmqpTemplate.receiveAndConvert()), FulfillmentOrder.class);

				FulfillmentOrderEntity entity1 = modelMapper.map(xmlOrder, FulfillmentOrderEntity.class);
				FulfillmentOrderEntity entity2 = null;
				try {
					entity2 = consumerMsgXmlRepo.save(entity1);
					list.add(xmlOrder);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} finally {
					if (entity2 == null) {
						System.out.println("XML Db Data is null");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	// #Query to Consume Json msg to rabbit queue
	@Override
	public ResponseEntity<List<OrderMsgConsumerJson>> consumeJsonMsg() {
		List<OrderMsgConsumerJson> jsonlist = new ArrayList<>();
		Properties properties = rabbitAdmin.getQueueProperties(ConsumerMsgConfig.Json_Queue_Key);
		int reqCount = (Integer) (properties != null ? properties.get(RabbitAdmin.QUEUE_MESSAGE_COUNT) : 0);
		for (int i = 0; i < reqCount; i++) {
			try {
				OrderMsgConsumerJson orderMessageJson = new ObjectMapper()
						.readValue(new String((byte[]) jsonAmqpTemplate.receiveAndConvert()), OrderMsgConsumerJson.class);

				OrderMsgConsumerJsonEntity entity1 = modelMapper.map(orderMessageJson, OrderMsgConsumerJsonEntity.class);
				OrderMsgConsumerJsonEntity entity2 = null;
				try {
					entity2 = consumerMsgJsonRepo.save(entity1);
					jsonlist.add(orderMessageJson);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} finally {
					if (entity2 == null) {
						System.out.println("JSON Db Data is null");
					}
				}
			} catch (Exception e) {
				break;
			}
		}
		return new ResponseEntity<>(jsonlist, HttpStatus.OK);
	}
}
