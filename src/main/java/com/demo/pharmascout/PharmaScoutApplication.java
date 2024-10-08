package com.demo.pharmascout;

import com.demo.pharmascout.medicine.expired.service.ExpiredMedicineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@EnableJpaAuditing
@SpringBootApplication
public class PharmaScoutApplication {

	public static void main ( String[] args ) {
		SpringApplication.run(PharmaScoutApplication.class, args);
		log.info("PharmaScout application started successfully at http://localhost:6969/");
	}

}
