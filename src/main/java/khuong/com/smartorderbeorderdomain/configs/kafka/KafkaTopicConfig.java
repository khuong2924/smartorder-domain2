package khuong.com.smartorderbeorderdomain.configs.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.errors.TopicExistsException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public AdminClient adminClient() {
        Map<String, Object> configs = Collections.singletonMap(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return AdminClient.create(configs);
    }

    @Bean
    public NewTopic createTopic(AdminClient adminClient) throws ExecutionException, InterruptedException {
        String topicName = "mykafka";
        if (!topicExists(adminClient, topicName)) {
            return new NewTopic(topicName, 1, (short) 1);
        } else {
            return null;
        }
    }

    private boolean topicExists(AdminClient adminClient, String topicName) throws ExecutionException, InterruptedException {
        return adminClient.listTopics().names().get().contains(topicName);
    }
}