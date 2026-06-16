package com.example.graphql_angular;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GraphqlAngularApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraphqlAngularApplication.class, args);
	}

}
