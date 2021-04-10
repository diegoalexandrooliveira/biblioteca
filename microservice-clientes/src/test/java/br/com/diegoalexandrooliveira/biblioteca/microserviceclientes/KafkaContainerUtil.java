package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Configuration
class KafkaContainerUtil {

    private static final KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.1.0"));
    private static final GenericContainer<?> schemaRegistry = new GenericContainer<>(DockerImageName.parse("confluentinc/cp-schema-registry:6.1.0"));
//    private static final GenericContainer<?> kafDrop = new GenericContainer<>(DockerImageName.parse("obsidiandynamics/kafdrop:latest"));

    @Value("${kafka.producer.topic}")
    private String topico;


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

//        kafDrop
//                .withNetwork(network)
//                .withExposedPorts(9000)
//                .withEnv(Map.of("KAFKA_BROKERCONNECT", kafkaContainer.getNetworkAliases().get(0) + ":9092", "CMD_ARGS", String.format("--schemaregistry.connect=http://%s:8081", schemaRegistry.getNetworkAliases().get(0))))
//                .start();
    }

    @Bean
    public ProducerFactory<Object, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        config.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, String.format("http://%s:%s", schemaRegistry.getContainerIpAddress(), schemaRegistry.getFirstMappedPort()));

        AdminClient adminClient = KafkaAdminClient.create(config);
        adminClient.createTopics(List.of(new NewTopic(topico, 1, (short) 1)));

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public Consumer<StringDeserializer, KafkaAvroDeserializer> kafkaConsumerConfig() {
        HashMap<String, Object> config = new HashMap<>();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 5);
        config.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        config.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, String.format("http://%s:%s", schemaRegistry.getContainerIpAddress(), schemaRegistry.getFirstMappedPort()));
        config.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());

        KafkaConsumer<StringDeserializer, KafkaAvroDeserializer> consumer = new KafkaConsumer<>(config);
        consumer.subscribe(List.of(topico));
        return consumer;
    }
}
