package com.order.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.Jackson2XmlMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumerMsgConfig {

	public static final String Json_Queue_Key = "JsonMsgProducerQueue";
	public static final String XML_Queue_Key = "XMLMsgProducerQueue";
	public static final String Exchange_Key = "OrderMsgConsumerExchange";
	public static final String Routing_Key = "OrderMsgConsumerRouting";

	@Bean(name = "CONSUMER_JSON_Q")
	public Queue jsonProducerQueue() {
		return new Queue(Json_Queue_Key);
	}

	@Bean(name = "CONSUMER_XML_Q")
	public Queue xmlProducerQueue() {
		return new Queue(XML_Queue_Key);
	}

//	@Bean
//	public TopicExchange exchange() {
//		return new TopicExchange(Exchange_Key);
//	}

	@Bean
	DirectExchange exchange() {
		return new DirectExchange(Exchange_Key);
	}

	@Bean
	Binding jsonProducerBinding(DirectExchange exchange) {
		return BindingBuilder.bind(jsonProducerQueue()).to(exchange).with(jsonProducerQueue().getName());
	}

	@Bean
	Binding xmlProducerBinding(DirectExchange exchange) {
		return BindingBuilder.bind(xmlProducerQueue()).to(exchange).with(xmlProducerQueue().getName());
	}

	@Bean("CONSUMER_RabbitAdmin")
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	@Bean("CONSUMER_JsonAmqpTemplate")
	public AmqpTemplate jsonTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setRoutingKey(Routing_Key);
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		rabbitTemplate.setDefaultReceiveQueue(jsonProducerQueue().getName());
		return rabbitTemplate;
	}

	@Bean("CONSUMER_XMLAmqpTemplate")
	public AmqpTemplate xmlTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setRoutingKey(Routing_Key);
		rabbitTemplate.setMessageConverter(new Jackson2XmlMessageConverter());
		rabbitTemplate.setDefaultReceiveQueue(xmlProducerQueue().getName());
		return rabbitTemplate;
	}

}
