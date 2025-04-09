package com.api.worker;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableDiscoveryClient
public class WorkerApplication {

	private final Environment environment;

    public WorkerApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
		SpringApplication.run(WorkerApplication.class, args);
	}

	@PostConstruct
	public void init() {
		String port = environment.getProperty("server.port");
		System.out.println("Running on port: " + port);
	}
}
