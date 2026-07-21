package com.awbp;

import com.awbp.messaging.Kafka;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic downloadRequestsTopic() {
        return TopicBuilder
                .name(Kafka.Topics.DOWNLOAD_REQUESTS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic deliveryRequestsTopic() {
        return TopicBuilder
                .name(Kafka.Topics.DELIVERY_REQUESTS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic downloadStatusChangesTopic() {
        return TopicBuilder
                .name(Kafka.Topics.DOWNLOAD_STATUS_CHANGES)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
