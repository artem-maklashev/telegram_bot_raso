package ru.artemmaklashev.telegram_bot_raso;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.artemmaklashev.telegram_bot_raso.config.TelegramConfig;
import ru.artemmaklashev.telegram_bot_raso.controller.TelegramController;



@Component
public class Bot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer{

    @Value("${telegram.bot.token}")
    private String token;
    private final TelegramController telegramController;

    @Autowired
    public Bot(TelegramController telegramController) {
        this.telegramController = telegramController;   }


    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() || update.hasCallbackQuery()) {
            telegramController.handleUpdate(update);
        }
    }


}

