package com.awbp;

import com.awbp.contact.DownloadRequestedEvent;
import com.awbp.messaging.Kafka;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DownloadRequestedListener {

    private final DownloadProcessor processor;

    @KafkaListener(
            topics = Kafka.Topics.DOWNLOAD_REQUESTS,
            groupId = Kafka.ConsumerGroups.DOWNLOAD_WORKERS
    )
    public void handle(DownloadRequestedEvent event) {
        log.info("Received: {}", event);
        processor.process(event.jobId());
        log.info("Completed: {}", event);

    }

}