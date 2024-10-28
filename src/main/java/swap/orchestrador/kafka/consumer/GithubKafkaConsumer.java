package swap.orchestrador.kafka.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import swap.orchestrador.client.GithubClient;
import swap.orchestrador.service.GithubService;
import swap.producer.user.GithubUser;

@Service
public class GithubKafkaConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(GithubClient.class);

    @Value("${app.kafka.consumer.key.message}")
    private String MESSAGE_KEY;
    public final GithubService githubService;

    public GithubKafkaConsumer(GithubService githubService) {
        this.githubService = githubService;
    }

    @KafkaListener(topics = "USER-REPOSITORY-GITHUB-TOPIC", groupId = "consumer-group-id")
    public void consume(ConsumerRecord<String,
            GenericRecord> consumerRecord,
                        @Header(KafkaHeaders.RECEIVED_KEY) String MESSAGE_KEY) {
        if (ObjectUtils.isEmpty(consumerRecord)) {
            LOG.warn("Not message, Key_message: MESSAGE_KEY");
        }
        LOG.warn("New messagem from kafka conumer");
        GithubUser githubUser = (GithubUser) consumerRecord.value();
        githubService.managerGithubInfo(
                githubUser.getUser().toString(), githubUser.getRepository().toString());
    }

}
