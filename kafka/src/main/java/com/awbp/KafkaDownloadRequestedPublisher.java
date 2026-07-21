package com.awbp;

import com.awbp.contact.DownloadRequestedEvent;
import com.awbp.messaging.Kafka;
import com.awbp.ports.DownloadRequestedPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaDownloadRequestedPublisher implements DownloadRequestedPublisher {

    private final KafkaTemplate<String, DownloadRequestedEvent> kafkaTemplate;

    @Override
    public void publish(DownloadRequestedEvent event) {
        kafkaTemplate.send(Kafka.Topics.DOWNLOAD_REQUESTS, event.jobId().toString(), event);
    }
}
