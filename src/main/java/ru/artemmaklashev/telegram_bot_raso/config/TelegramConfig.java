package ru.artemmaklashev.telegram_bot_raso.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "telegram")
@Getter
@Setter
public class TelegramConfig {

    private NotificationConfig notification; // Вложенные свойства "telegram.notification"
    private BotConfig bot;                   // Вложенные свойства "telegram.bot"

    @Getter
    @Setter
    public static class NotificationConfig {
        private List<String> chatIds; // Привязка к "telegram.notification.chat-ids"
    }

    @Getter
    @Setter
    public static class BotConfig {
        private String name;  // Привязка к "telegram.bot.name"
        private String token; // Привязка к "telegram.bot.token"
    }

    @Bean
    public OkHttpTelegramClient telegramClient() {
        if (bot.getToken() == null || bot.getToken().isEmpty()) {
            throw new IllegalStateException("Bot token must be provided");
        }
        return new OkHttpTelegramClient(bot.getToken());
    }
}
