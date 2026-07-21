package com.awbp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateDispatcher {

    private final CommandHandler commandHandler;

    public void dispatch(Update update) throws TelegramApiException {

        if (update.hasMessage()) {
            commandHandler.handle(update.getMessage());
        }
    }

    public void dispatch(List<Update> updates) throws TelegramApiException {

        for (Update update : updates) {
            if (update.hasMessage()) {
                commandHandler.handle(update.getMessage());
            }
        }
    }
}
