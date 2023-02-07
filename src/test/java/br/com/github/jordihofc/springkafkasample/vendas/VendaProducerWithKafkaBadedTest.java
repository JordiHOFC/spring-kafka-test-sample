package br.com.github.jordihofc.springkafkasample.vendas;

import br.com.github.jordihofc.springkafkasample.base.KafkaBasedIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VendaProducerWithKafkaBadedTest extends KafkaBasedIntegrationTest {
    @Autowired
    private VendaProducer producer;

    @Test
    @DisplayName("deve consumir uma mensagem na fila")
    void t1() {
        //cenario
        Venda venda = new Venda("PlayStation 5", BigDecimal.valueOf(5000), LocalDate.now());

        //acao
        producer.send(venda);

        //validacao
        List<Venda> records = getRecordsByTopic(Venda.class);
        assertThat(records)
                .hasSize(1)
                .allMatch(record-> record.equals(venda));

    }
}
