package com.pedrobne.salesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SalesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesServiceApplication.class, args);
	}

	@RestController
	@RequestMapping("sales")
	public static class OlaController {
		@RequestMapping
		public String hello() {
			return "Olá, sales!";
		}
	}
}
