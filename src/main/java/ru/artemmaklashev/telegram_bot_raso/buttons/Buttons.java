package ru.artemmaklashev.telegram_bot_raso.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

public class Buttons {
    private final List<InlineKeyboardRow> rows = new ArrayList<>();

    public void addButton(Button button, int rowNumber) {
        InlineKeyboardButton btn = button.build();

        // Проверяем, есть ли уже нужная строка
        while (rows.size() < rowNumber) {
            rows.add(new InlineKeyboardRow()); // Создаем пустую строку
        }

        // Добавляем кнопку в нужную строку
        rows.get(rowNumber - 1).add(btn);
    }

    public InlineKeyboardMarkup build() {
        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }
}


