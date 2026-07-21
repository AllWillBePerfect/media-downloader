package com.awbp;

import com.awbp.domain.DeliveryType;
import com.awbp.domain.DownloadJob;
import com.awbp.domain.DownloadedFile;
import com.awbp.ports.DownloadedFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class TelegramDeliveryHandler
        implements DeliveryHandler {

    private static final long TELEGRAM_FILE_LIMIT = 50L * 1024 * 1024;

    private final TelegramSender telegramSender;

    private final DownloadedFileRepository downloadedFileRepository;
    private final ExpiredFileCleanupService fileCleanupService;

    @Value("${app.download.public-base-url}")
    private String publicBaseUrl;

    @Override
    public DeliveryType supports() {
        return DeliveryType.TELEGRAM;
    }

    @Override
    public void deliver(
            DownloadJob job,
            Path file,
            Long size,
            String target
    ) {
        DownloadedFile downloadedFile = downloadedFileRepository
                .getByJobId(job.id())
                .orElseThrow();

        downloadedFileRepository.save(
                downloadedFile.copyWithExpiresAt(
                        Instant.now().plus(Duration.ofMinutes(30))
                )
        );

        Long chatId = Long.valueOf(target);

        if (size >= TELEGRAM_FILE_LIMIT) {
            telegramSender.sendMessage(
                    chatId,
                    """
                    Видео слишком большое для отправки через Telegram.

                    Скачать можно по ссылке:
                    """ + buildDownloadUrl(job)
            );

            return;
        }

        telegramSender.sendVideo(chatId, file.toFile());

        fileCleanupService.deleteNow(downloadedFile);
    }

    private String buildDownloadUrl(DownloadJob job) {
        return publicBaseUrl
                + "/api/v1/downloads/"
                + job.id()
                + "/file";
    }

}