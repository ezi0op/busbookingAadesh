package com.ash.bbus.web.BusTicketBookingSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync // ✅ For async email sending
@EnableScheduling // ✅ For seat lock expiry scheduler
public class BusTicketBookingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusTicketBookingSystemApplication.class, args);
	}
}