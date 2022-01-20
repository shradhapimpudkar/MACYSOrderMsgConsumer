package com.order.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import com.order.repository.ConsumerMsgJsonRepo;
import com.order.repository.ConsumerMsgXmlRepo;

@Component
public class ConsumerActuator implements InfoContributor {

	@Autowired
	ConsumerMsgJsonRepo consumerMsgJsonRepo;

	@Autowired
	ConsumerMsgXmlRepo consumerMsgXmlRepo;

	@Override
	public void contribute(Info.Builder builder) {
		builder.withDetail("total-consumer-xml-users", consumerMsgXmlRepo.count())// xml
		.withDetail("total-consumer-json-users", consumerMsgJsonRepo.count())// json
		.build();
	}
}
