package com.awbp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TelegramUpdateConsumer implements LongPollingUpdateConsumer {

    private final UpdateDispatcher dispatcher;

    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            if (!update.hasMessage()) {
                continue;
            }

            try {
                dispatcher.dispatch(update);
                log.info(
                        "Processed Telegram update {} for chat {}",
                        update.getUpdateId(),
                        update.getMessage().getChatId()
                );
            } catch (RuntimeException | TelegramApiException e) {
                log.error("Failed to process Telegram update {}", update.getUpdateId(), e);
            }
        }
    }
}
