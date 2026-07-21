package com.awbp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Configuration
public class TelegramAppConfig {

    @Bean
    BotSession botSession(
            @Value("${telegram.bot.token}")
            String token,
            TelegramUpdateConsumer consumer
    ) throws TelegramApiException {

        TelegramBotsLongPollingApplication application =
                new TelegramBotsLongPollingApplication();

        return application.registerBot(token, consumer);
    }

    @Bean
    public RestClient restClient(
            @Value("${app.rest.base-url}") String restBaseUrl
    ) {
        return RestClient.builder()
                .baseUrl(restBaseUrl)
                .build();
    }
}
