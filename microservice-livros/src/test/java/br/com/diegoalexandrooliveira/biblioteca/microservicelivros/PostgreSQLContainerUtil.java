package br.com.diegoalexandrooliveira.biblioteca.microservicelivros;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Configuration
class PostgreSQLContainerUtil {

    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13-alpine");

    static {
        postgreSQLContainer.start();
    }

    @Bean
    DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .url(postgreSQLContainer.getJdbcUrl())
                .build();
    }
}
