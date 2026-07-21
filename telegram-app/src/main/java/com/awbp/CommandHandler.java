package com.awbp;

import com.awbp.domain.DeliveryType;
import com.awbp.domain.DownloadType;
import com.awbp.rest.dto.CreateDownloadRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final TelegramSenderImpl sender;
    private final RestClient restClient;

    public void handle(Message message) throws TelegramApiException {

        if ("/start".equals(message.getText())) {

            sender.sendMessage(
                    message.getChatId(),
                    "Добро пожаловать!"
            );

            restClient.post()
                    .uri("/api/v1/downloads")
                    .body(
                            new CreateDownloadRequest(
                                    DeliveryType.TELEGRAM,
                                    message.getFrom().getId(),
                                    "https://www.youtube.com/watch?v=xvFZjo5PgG0",
                                    DownloadType.VIDEO,
                                    message.getChatId().toString()
                            )
                    )
                    .retrieve()
                    .toBodilessEntity();

        } else {
            restClient.post()
                    .uri("/api/v1/downloads")
                    .body(
                            new CreateDownloadRequest(
                                    DeliveryType.TELEGRAM,
                                    message.getFrom().getId(),
                                    message.getText(),
                                    DownloadType.VIDEO,
                                    message.getChatId().toString()
                            )
                    )
                    .retrieve()
                    .toBodilessEntity();
        }
    }
}