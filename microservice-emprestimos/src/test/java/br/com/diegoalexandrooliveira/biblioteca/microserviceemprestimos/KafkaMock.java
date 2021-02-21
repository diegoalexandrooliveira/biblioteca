package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaMock {


    @Bean
    KafkaConsumer kafkaConsumer(){
        return null;
    }

}
