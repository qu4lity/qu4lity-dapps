package it.eng.sidrestapi;

import it.eng.sidrestapi.controller.SidRestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class SidRestApiApplication {

	private static final Logger log = Logger.getLogger(SidRestApiApplication.class.getName());


	public static void main(String[] args) {
		SpringApplication.run(SidRestApiApplication.class, args);
		log.info("Server started at : localhost:8080/swagger-ui.html " );

	}

}
