package com.ecommerce.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OrdersApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrdersApplication.class, args);
		System.out.println("Order Service Application has started....");
	}

}
