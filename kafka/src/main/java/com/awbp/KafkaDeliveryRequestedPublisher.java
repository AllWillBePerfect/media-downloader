package com.awbp;

import com.awbp.contact.DeliveryRequestedEvent;
import com.awbp.messaging.Kafka;
import com.awbp.ports.DeliveryRequestedPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaDeliveryRequestedPublisher
        implements DeliveryRequestedPublisher {

    private final KafkaTemplate<String, DeliveryRequestedEvent> kafkaTemplate;

    @Override
    public void publish(DeliveryRequestedEvent event) {
        kafkaTemplate.send(
                Kafka.Topics.DELIVERY_REQUESTS,
                event.jobId().toString(),
                event
        );
    }
}
