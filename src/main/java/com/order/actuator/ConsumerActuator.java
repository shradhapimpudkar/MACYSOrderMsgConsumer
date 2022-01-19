package com.order.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;

import com.order.repository.ConsumerMsgJsonRepo;
import com.order.repository.ConsumerMsgXmlRepo;

public class ConsumerActuator implements InfoContributor {

	@Autowired
	ConsumerMsgJsonRepo consumerMsgJsonRepo;

	@Autowired
	ConsumerMsgXmlRepo consumerMsgXmlRepo;

	@Override
	public void contribute(Info.Builder builder) {
		builder.withDetail("total-consumer-json-users", consumerMsgXmlRepo.count())// xml
		.withDetail("total-consumer-xml-users", consumerMsgJsonRepo.count())// json
		.build();
	}
}
