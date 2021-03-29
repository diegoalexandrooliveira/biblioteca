package br.com.diegoalexandrooliveira.biblioteca.microservicelivros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroserviceLivrosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceLivrosApplication.class, args);
	}

}
