package com.aluracursos.literatura_desafio;

import com.aluracursos.literatura_desafio.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaDesafioApplication implements CommandLineRunner {
	@Autowired
	private Principal principal;

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaDesafioApplication.class, args);

	}
	@Override
	public void run(String... args) throws Exception{
		// Este método se ejecutará una vez que la aplicación Spring Boot esté lista
		principal.exhibirMenu();
	}

}
