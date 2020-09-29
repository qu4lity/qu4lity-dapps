package it.eng.sidcommandline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SidCommandLineApplication implements CommandLineRunner {

	@Autowired
	private SidProcessor sidProcessor;

	public static void main(String[] args) {
		SpringApplication.run(SidCommandLineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
			sidProcessor.process(args);
	}
}
