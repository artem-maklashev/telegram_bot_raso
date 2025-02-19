package ru.artemmaklashev.telegram_bot_raso.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class CommandHandler {
    public SendMessage handleCommand(String chatId, String text) {
        if ("/start".equalsIgnoreCase(text)) {
            return createStartMessage(chatId);
        } else if ("/help".equalsIgnoreCase(text)) {
            return createHelpMessage(chatId);
        } else {
            return createUnknownCommandMessage(chatId);
        }
    }

    private SendMessage createStartMessage(String chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Добро пожаловать! Используйте /help для списка доступных команд.")
                .build();
    }

    private SendMessage createHelpMessage(String chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Список команд:\n/start - начать работу\n/help - помощь")
                .build();
    }

    private SendMessage createUnknownCommandMessage(String chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("Неизвестная команда. Используйте /help для списка доступных команд.")
                .build();
    }
}

