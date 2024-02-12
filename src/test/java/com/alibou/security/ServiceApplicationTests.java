package com.alibou.security;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ServiceApplicationTests {
    @LocalServerPort
    private Integer port;
    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("jwt_security");

    @BeforeAll
    static void setup() {
        postgreSQLContainer.start();
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        String username = postgreSQLContainer.getUsername();
        String password = postgreSQLContainer.getPassword();

        // Set up the necessary tables and data using JDBC or an ORM of your choice
        // Here's an example using Spring JDBC template
        JdbcTemplate jdbcTemplate = new JdbcTemplate(
                DataSourceBuilder.create()
                        .url(jdbcUrl)
                        .username(username)
                        .password(password)
                        .build());
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }


    @Test
    public void sayHello_ReturnsHelloWorld() {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "http://localhost:" + port + "/sayhello";
        String response = restTemplate.getForObject(resourceUrl, String.class);
        assertThat(response).isEqualTo("Hello World!");
    }
}