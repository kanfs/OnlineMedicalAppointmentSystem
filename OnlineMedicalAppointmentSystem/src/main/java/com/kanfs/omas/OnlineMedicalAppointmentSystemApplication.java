package com.kanfs.omas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class OnlineMedicalAppointmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineMedicalAppointmentSystemApplication.class, args);
	}

}
