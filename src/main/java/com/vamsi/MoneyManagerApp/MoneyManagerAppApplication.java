package com.vamsi.MoneyManagerApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoneyManagerAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyManagerAppApplication.class, args);
	}

}
