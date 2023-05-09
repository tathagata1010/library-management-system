package com.dev.library_management;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryManagementApplication {

	private static final Logger logger = LoggerFactory.getLogger(LibraryManagementApplication.class);


	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementApplication.class, args);
		logger.info("hello");
	}

}
