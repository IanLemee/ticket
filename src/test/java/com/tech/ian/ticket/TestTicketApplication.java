package com.tech.ian.ticket;

import org.springframework.boot.SpringApplication;

public class TestTicketApplication {

	public static void main(String[] args) {
		SpringApplication.from(TicketApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
