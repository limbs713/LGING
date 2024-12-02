package com.lge.connected;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ConnectedApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConnectedApplication.class, args);
	}

}
