package swap.orchestrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.avro.generic.GenericRecord;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProducerMessageDTO {
    private String messageKey;
    private String topic;
    private GenericRecord record;
}
