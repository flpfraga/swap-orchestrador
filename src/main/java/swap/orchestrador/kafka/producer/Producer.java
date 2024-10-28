package swap.orchestrador.kafka.producer;

import org.apache.avro.specific.SpecificRecordBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import swap.orchestrador.dto.ProducerMessageDTO;

@Service
public class Producer {
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    public Producer(KafkaTemplate<String, SpecificRecordBase> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void producer(ProducerMessageDTO producerMessageDTO) {
        kafkaTemplate.send(MessageBuilder
                .withPayload(producerMessageDTO.getRecord())
                .setHeader(KafkaHeaders.TOPIC, producerMessageDTO.getTopic())
                .setHeader("kafka_messageKey", producerMessageDTO.getMessageKey())
                .build());

    }
}
