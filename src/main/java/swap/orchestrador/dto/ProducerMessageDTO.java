package swap.orchestrador.dto;

import org.apache.avro.generic.GenericRecord;

public class ProducerMessageDTO {
    private String messageKey;
    private String topic;
    private GenericRecord record;

    public ProducerMessageDTO(String messageKey, String topic, GenericRecord record) {
        this.messageKey = messageKey;
        this.topic = topic;
        this.record = record;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public GenericRecord getRecord() {
        return record;
    }

    public void setRecord(GenericRecord record) {
        this.record = record;
    }
}
