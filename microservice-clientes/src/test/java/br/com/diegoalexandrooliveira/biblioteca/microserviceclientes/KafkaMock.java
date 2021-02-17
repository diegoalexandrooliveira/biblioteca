package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes;

import org.apache.kafka.clients.producer.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

@Configuration
public class KafkaMock {

    @Bean
    KafkaTemplate kafkaTemplate() {
        ProducerFactory<String, Object> producerFactory = new ProducerFactory<>() {
            @Override
            public Producer<String, Object> createProducer() {
                return null;
            }
        };
        return new KafkaTemplateMock(producerFactory);
    }

    private class KafkaTemplateMock extends KafkaTemplate {

        public KafkaTemplateMock(ProducerFactory producerFactory) {
            super(producerFactory);
        }

        @Override
        public ListenableFuture<SendResult> send(String topic, Object data) {
            return null;
        }
    }
}
