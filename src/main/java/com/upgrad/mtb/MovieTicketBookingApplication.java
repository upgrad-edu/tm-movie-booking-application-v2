package com.upgrad.mtb;

import com.upgrad.mtb.services.InitService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MovieTicketBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieTicketBookingApplication.class, args);

	}

	@Bean
	CommandLineRunner init (InitService initService){
		return args -> {
			initService.init();
		};
	}


}
