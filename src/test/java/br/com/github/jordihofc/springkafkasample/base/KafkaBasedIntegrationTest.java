package br.com.github.jordihofc.springkafkasample.base;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.time.Duration.*;

@SpringBootTest
@EmbeddedKafka(
        topics = "${spring.kafka.producer.topic}",
        partitions = 1,
        bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
public abstract class KafkaBasedIntegrationTest {
    @Autowired
    private EmbeddedKafkaBroker broker;
    @Value("${spring.kafka.producer.topic}")
    private String topic;


    private <V> Consumer<String, V> createConsumer(String topic, Class<V> value) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(topic, "true", this.broker);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        DefaultKafkaConsumerFactory<String, V> consumerFactory = new DefaultKafkaConsumerFactory(
                consumerProps, new StringDeserializer(), new JsonDeserializer<>()
        );
        return consumerFactory.createConsumer();
    }

    public <V> List<V> getRecordsByTopic(Class<V> value) {
        try (Consumer<String, V> consumer = createConsumer(topic, value)) {

            this.broker.consumeFromEmbeddedTopics(consumer, topic);

            Iterable<ConsumerRecord<String, V>> recordsIterable = KafkaTestUtils.getRecords(consumer, ofSeconds(5));

            return StreamSupport.stream(recordsIterable.spliterator(), false)
                    .map(ConsumerRecord::value)
                    .collect(Collectors.toList());
        }

    }

}
