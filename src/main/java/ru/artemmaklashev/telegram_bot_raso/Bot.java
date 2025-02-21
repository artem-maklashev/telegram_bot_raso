package ru.artemmaklashev.telegram_bot_raso;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import ru.artemmaklashev.telegram_bot_raso.config.TelegramConfig;
import ru.artemmaklashev.telegram_bot_raso.controller.TelegramController;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.TelegramUserService;


@Component
public class Bot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    @Value("${telegram.bot.token}")
    private String token;
    private final TelegramController telegramController;
    private final TelegramUserService telegramUserService;

    @Autowired
    public Bot(TelegramController telegramController, TelegramUserService telegramUserService) {
        this.telegramController = telegramController;
        this.telegramUserService = telegramUserService;
    }


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
        if (update.hasMessage()) {
            // Обработка обычных сообщений
            telegramController.handleUpdate(update);
        } else if (update.hasCallbackQuery()) {
            // Обработка callback-запросов
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            if (data.startsWith("authorize_") || data.startsWith("deny_")) {
                System.out.println("Перенаправление  на обработку запроса в TelegramUserService");
                telegramUserService.handleCallbackQuery(callbackQuery); // Передаем управление вашему обработчику
            } else {
                // Обработка других callback-запросов
                telegramController.handleUpdate(update);

            }
        }
    }
}

