package br.com.github.jordihofc.springkafkasample.vendas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VendaProducer {
    @Value("${spring.kafka.producer.topic}")
    private String topic;
    private static final Logger LOGGER = LoggerFactory.getLogger(VendaProducer.class);
    @Autowired
    private KafkaTemplate<String, Venda> templateMessage;

    public void send(Venda venda){
        templateMessage.send(topic, venda);
        LOGGER.info("Venda registrada com sucesso !{}", venda);
    }
}
