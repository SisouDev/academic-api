package com.institution.management.academic_api;

import org.springframework.boot.SpringApplication;

public class TestAcademicApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(AcademicApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
