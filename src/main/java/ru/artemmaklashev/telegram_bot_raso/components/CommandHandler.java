package ru.artemmaklashev.telegram_bot_raso.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class CommandHandler {

    private final Keyboards keyboards;
    @Autowired
    public CommandHandler(Keyboards keyboards) {
        this.keyboards = keyboards;
    }

    public SendMessage handleCommand(String chatId, String text) {
        System.out.println("Получено сообщение: " + text);
        if ("/start".equalsIgnoreCase(text) || "/start@raso_helper_bot".equalsIgnoreCase(text)) {
            return createStartMessage(chatId);
        } else if ("/help".equalsIgnoreCase(text) || "/help@raso_helper_bot".equalsIgnoreCase(text)) {
            return createHelpMessage(chatId);
        } else {
            return createUnknownCommandMessage(chatId);
        }
    }

    private SendMessage createStartMessage(String chatId) {
        InlineKeyboardMarkup keyboard = keyboards.getReportKeyboard();
        return SendMessage.builder()
                .chatId(chatId)
                .text("Добро пожаловать!")
                .replyMarkup(keyboard)
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

