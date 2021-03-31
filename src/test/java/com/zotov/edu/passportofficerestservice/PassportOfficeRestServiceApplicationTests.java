package com.zotov.edu.passportofficerestservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class PassportOfficeRestServiceApplicationTests {

	public static final PostgreSQLContainer<?> postgreSqlContainer = new PostgreSQLContainer<>("postgres");

	@Test
	void contextLoads() {
	}

}
