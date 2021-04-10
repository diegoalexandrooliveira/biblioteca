package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

@Configuration
class KafkaContainerUtil {

    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.1.0"));
    private static final GenericContainer<?> schemaRegistry = new GenericContainer<>(DockerImageName.parse("confluentinc/cp-schema-registry:6.1.0"));
    private static final GenericContainer<?> kafDrop = new GenericContainer<>(DockerImageName.parse("obsidiandynamics/kafdrop:latest"));


    static {
        Network network = Network.newNetwork();
        kafkaContainer
                .withNetwork(network)
                .start();

        schemaRegistry
                .withNetwork(network)
                .withExposedPorts(8081)
                .withEnv(Map.of("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", kafkaContainer.getNetworkAliases().get(0) + ":9092", "SCHEMA_REGISTRY_HOST_NAME", "localhost"))
                .start();

        kafDrop
                .withNetwork(network)
                .withExposedPorts(9000)
                .withEnv(Map.of("KAFKA_BROKERCONNECT", kafkaContainer.getNetworkAliases().get(0) + ":9092", "CMD_ARGS", String.format("--schemaregistry.connect=http://%s:8081", schemaRegistry.getNetworkAliases().get(0))))
                .start();
    }

    @Bean
    public ProducerFactory<Object, Object> producerFactory() {
        HashMap<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        config.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, String.format("http://%s:%s", schemaRegistry.getContainerIpAddress(), schemaRegistry.getFirstMappedPort()));
        return new DefaultKafkaProducerFactory<>(config);
    }
}
