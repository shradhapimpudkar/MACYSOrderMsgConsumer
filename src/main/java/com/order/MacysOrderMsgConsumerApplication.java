package com.order;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.modelmapper.ModelMapper;

@SpringBootApplication
@EnableSwagger2
public class MacysOrderMsgConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MacysOrderMsgConsumerApplication.class, args);
	}

	@Bean
	public Docket getCustomizedDocket() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(getApiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.order")).paths(PathSelectors.any()).build();
	}

	private ApiInfo getApiInfo() {
		return new ApiInfo("Order Msg Consumer Application Api Document",
				"Api documentation for Order Msg Producer App", "1.0", "",
				new Contact("Shradha", "", "shradha.pimpudkar@zensar.com"), "SP", "www.sp.com", new ArrayList<>());
	}

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
}
