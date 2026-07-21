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
        try {
            dispatcher.dispatch(updates);
            for (Update update : updates) {
                log.info(update.getMessage().getChatId().toString());
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
