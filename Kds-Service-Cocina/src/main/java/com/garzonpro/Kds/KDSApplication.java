package com.garzonpro.Kds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KDSApplication {
	public static void main(String[] args) {
		SpringApplication.run(KDSApplication.class, args);
	}
}