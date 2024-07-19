package com.example.DentistryManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class DentistryManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DentistryManagementApplication.class, args);
	}

}
