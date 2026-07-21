package com.awbp;

import com.awbp.contact.DownloadStatusChangedEvent;
import com.awbp.domain.DeliveryTarget;
import com.awbp.domain.DownloadStatus;
import com.awbp.ports.DeliveryTargetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DownloadStatusNotifier {

    private final DeliveryTargetRepository deliveryTargetRepository;
    private final TelegramSender telegramSender;

    public void notify(DownloadStatusChangedEvent event) {
        String message = messageFor(event.status());

        if (message == null) {
            return;
        }

        DeliveryTarget target = deliveryTargetRepository
                .getByJobId(event.jobId())
                .orElseThrow();

        switch (target.deliveryType()) {
            case TELEGRAM -> telegramSender.sendMessage(
                    Long.valueOf(target.target()),
                    message
            );
        }
    }

    private String messageFor(DownloadStatus status) {
        return switch (status) {
            case QUEUED -> null;
            case DOWNLOADING -> "Выполняется скачивание файла.";
            case DOWNLOADED -> "Файл скачан. Подготовка к отправке.";
            case DELIVERING -> "Выполняется отправка файла.";
            case DELIVERED -> "Файл успешно доставлен.";
            case REJECTED -> "Задача отклонена: недостаточно свободного места.";
            case FAILED -> "Не удалось обработать файл.";
        };
    }
}