package com.cast_group.test_case;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class TestCaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestCaseApplication.class, args);
	}

}
