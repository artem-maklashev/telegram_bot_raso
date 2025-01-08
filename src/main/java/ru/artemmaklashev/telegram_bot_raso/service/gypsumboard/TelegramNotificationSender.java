package ru.artemmaklashev.telegram_bot_raso.service.gypsumboard;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.artemmaklashev.telegram_bot_raso.config.TelegramConfig;
import ru.artemmaklashev.telegram_bot_raso.controller.TelegramController;

import java.util.List;

@Component
public class TelegramNotificationSender {
    private final TelegramController telegramController;
    private final TelegramConfig config;


    public TelegramNotificationSender(TelegramController telegramController, TelegramConfig config) {
        this.telegramController = telegramController;
        this.config = config;
    }

    public void sendMessage(String message) {
        List<String> chatIds = config.getNotification().getChatIds();
        for (String chatId : chatIds) {
            SendMessage newMessage = new SendMessage(chatId, message);

            telegramController.executeMessage(newMessage); // Отправляем сообщение через TelegramController
        }
    }

}
