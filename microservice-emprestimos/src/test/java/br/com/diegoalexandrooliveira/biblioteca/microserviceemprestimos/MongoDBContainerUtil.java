package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.MongoDBContainer;

@Configuration
class MongoDBContainerUtil {

    private static final MongoDBContainer mongoContainer = new MongoDBContainer("mongo:4.2.12-bionic");

    static {
        mongoContainer.start();
    }

    @Bean
    MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoContainer.getReplicaSetUrl());
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }
}
