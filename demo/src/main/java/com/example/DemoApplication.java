package com.example;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(UserRepository userRepository) {
		return new CommandLineRunner() {
			@Override
			public void run(String... arg0) throws Exception {
				String[] names = { "Howard", "Leonard", "Raj", "Sheldon" };

				// Intentional bug
				String neverUsed = "";

				Arrays.stream(names).forEach(name -> userRepository.save(new User(name)));

				userRepository.findAll().stream().forEach(user -> System.out.println(user));

				System.out.println(userRepository.findAllByName("Raj"));
			}
		};
	}

	// Custom health indicator to show in health endpoint
	@Bean
	public HealthIndicator springBootDemo() {
		return () -> Health.status("Hola").withDetail("timestamp", System.currentTimeMillis()).build();
	}
}

@RepositoryRestResource
interface UserRepository extends JpaRepository<User, Long> {

	@RestResource(path = "by-name")
	Collection<User> findAllByName(@Param("name") String name);
}