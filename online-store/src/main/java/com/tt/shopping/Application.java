package com.tt.shopping;

import com.tt.shopping.init.ApplicationInitializationProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class Application {

	public static void main(String[] args) {
		final ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		final ApplicationInitializationProcessor initProcessor =
				context.getBean(ApplicationInitializationProcessor.class);
		initProcessor.init();
	}
}
