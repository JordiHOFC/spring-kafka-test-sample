package br.com.github.jordihofc.springkafkasample.vendas;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import static br.com.github.jordihofc.springkafkasample.vendas.VendaProducerTest.TOPIC;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@EmbeddedKafka(
    topics = "${spring.kafka.producer.topic}",
        partitions = 1,
        bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
public class VendaProducerTest {
    @Autowired
    private EmbeddedKafkaBroker broker;
    public final static String TOPIC = "venda";
    @Autowired
    private VendaProducer producer;

    @Test
    @DisplayName("deve consumir uma mensagem na fila")
    void t1() {
        //cenario
        Consumer<String, Venda> consumer = createConsumer(Venda.class);
        this.broker.consumeFromEmbeddedTopics(consumer, "venda");
        Venda venda = new Venda("PlayStation 5", BigDecimal.valueOf(5000), LocalDate.now());

        //acao
        producer.send(venda);

        //validacao
        ConsumerRecords<String, Venda> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertThat(records)
                .hasSize(1)
                .allMatch(msg -> msg.value().equals(venda));

    }

    private <V> Consumer<String, V> createConsumer(Class<V> classType) {

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(TOPIC, "true", this.broker);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        DefaultKafkaConsumerFactory<String, V> consumerFactory = new DefaultKafkaConsumerFactory(
                consumerProps, new StringDeserializer(), new JsonDeserializer<>()
        );

        return consumerFactory.createConsumer();
    }
}
