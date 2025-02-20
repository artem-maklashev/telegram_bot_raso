package ru.artemmaklashev.telegram_bot_raso.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

public class Button {
    private final String buttonText;
    private final String buttonAction;

    public Button(String buttonText,  String buttonAction) {
        this.buttonText = buttonText;
        this.buttonAction = buttonAction;
    }

    public InlineKeyboardButton build() {
        return InlineKeyboardButton.builder()
                .text(buttonText)
                .callbackData(buttonAction) // Callback, который вы обрабатываете
                .build();
    }

}
