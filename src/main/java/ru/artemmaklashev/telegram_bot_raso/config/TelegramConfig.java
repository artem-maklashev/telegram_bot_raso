package ru.artemmaklashev.telegram_bot_raso.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

@Configuration
public class TelegramConfig {
    @Value("${bot.token}")
    private String token;

    @Bean
    public OkHttpTelegramClient telegramClient() {
        if (token == null || token.isEmpty()) {
            throw new IllegalStateException("Bot token must be provided");
        }
        return new OkHttpTelegramClient(token);
    }



}

