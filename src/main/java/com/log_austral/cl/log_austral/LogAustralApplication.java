package com.log_austral.cl.log_austral;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class LogAustralApplication {

	public static void main(String[] args) {
		// carga las variables de entorno .env antes que Springboot
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

		SpringApplication.run(LogAustralApplication.class, args);
	}

}
