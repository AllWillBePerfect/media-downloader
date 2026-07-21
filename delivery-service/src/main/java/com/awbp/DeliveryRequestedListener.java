package com.awbp;


import com.awbp.contact.DeliveryRequestedEvent;
import com.awbp.messaging.Kafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryRequestedListener {

    private final DeliveryProcessor processor;

    @KafkaListener(
            topics = Kafka.Topics.DELIVERY_REQUESTS,
            groupId = Kafka.ConsumerGroups.DELIVERY_WORKERS
    )
    public void handle(DeliveryRequestedEvent event) {
        log.info("Received: {}", event);
        processor.process(event.jobId(), Path.of(event.filePath()), event.size());
        log.info("Completed: {}", event);
    }
}
