package com.tt.shopping;

import com.tt.shopping.product.impl.service.ProductManagementServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		ProductManagementServiceImpl productManagementService =  context.getBean(ProductManagementServiceImpl.class);
		productManagementService.createSampleProduct();
	}
}
