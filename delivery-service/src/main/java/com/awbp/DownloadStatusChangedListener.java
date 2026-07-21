package com.awbp;

import com.awbp.contact.DownloadStatusChangedEvent;
import com.awbp.messaging.Kafka;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DownloadStatusChangedListener {

    private final DownloadStatusNotifier notifier;

    @KafkaListener(
            topics = Kafka.Topics.DOWNLOAD_STATUS_CHANGES,
            groupId = Kafka.ConsumerGroups.STATUS_NOTIFIERS
    )
    public void handle(DownloadStatusChangedEvent event) {
        notifier.notify(event);
    }
}
