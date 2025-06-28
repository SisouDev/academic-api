package com.institution.management.academic_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AcademicApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademicApiApplication.class, args);
	}

}
