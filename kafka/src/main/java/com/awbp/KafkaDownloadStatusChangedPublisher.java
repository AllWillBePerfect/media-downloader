package com.awbp;

import com.awbp.contact.DownloadStatusChangedEvent;
import com.awbp.messaging.Kafka;
import com.awbp.ports.DownloadStatusChangedPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaDownloadStatusChangedPublisher
        implements DownloadStatusChangedPublisher {

    private final KafkaTemplate<String, DownloadStatusChangedEvent> kafkaTemplate;

    @Override
    public void publish(DownloadStatusChangedEvent event) {
        kafkaTemplate.send(
                Kafka.Topics.DOWNLOAD_STATUS_CHANGES,
                event.jobId().toString(),
                event
        );
    }
}