package ru.artemmaklashev.telegram_bot_raso.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

public class Buttons {
    private final Long chatId;
    private final TelegramClient client;
    private final List<InlineKeyboardRow> rows = new ArrayList<>();
    private final InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(rows);

    public Buttons(Long chatId,  TelegramClient client) {
        this.chatId = chatId;
        this.client = client;
    }

    public void addButton(Button button, int rowNumber) {

        InlineKeyboardButton btn = button.build();

        // Проверяем, существует ли строка для кнопки
        while (rows.size() < rowNumber) {
            rows.add(new InlineKeyboardRow()); // Создаем пустую строку
        }

        // Добавляем кнопку в указанную строку
        rows.get(rowNumber - 1).add(btn);
    }

    public InlineKeyboardMarkup build() {
        return new InlineKeyboardMarkup(rows);
    }

    public void sendTextWithButtons() {

        // Создаем разметку клавиатуры

        keyboard.setKeyboard(rows);

        // Создаем сообщение
        SendMessage message = SendMessage.builder()
                .chatId(this.chatId.toString())
                .replyMarkup(keyboard)
                .build();

        // Отправляем сообщение
        try {
            this.client.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
