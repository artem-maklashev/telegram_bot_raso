package ru.artemmaklashev.telegram_bot_raso.buttons;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

public class Button extends InlineKeyboardButton{




    private final String buttonText;
    private final String buttonAction;

    public Button(String buttonText,  String buttonAction) {
        super(buttonText);
        this.buttonText = buttonText;
        this.buttonAction = buttonAction;
    }

    public InlineKeyboardButton build() {
        return InlineKeyboardButton.builder()
                .text(buttonText)
                .callbackData(buttonAction) // Callback, который вы обрабатываете
                .build();
    }

//    public void sendTextWithButtons() {
//        // Создаем сообщение
//        SendMessage message = SendMessage.builder()
//                .chatId(this.chatId.toString())
//                .text(this.text)
//                .build();
//
//        // Создаем кнопки
//        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(new ArrayList<>());
//
//        // Список строк кнопок
//        List<InlineKeyboardRow> rows = new ArrayList<>();
//
//
//        InlineKeyboardButton button1 = InlineKeyboardButton.builder()
//                .text(buttonText)
//                .callbackData(buttonAction) // Callback, который вы обрабатываете
//                .build();
//        InlineKeyboardRow row = new InlineKeyboardRow();
//        row.add(button1);
//        rows.add(row);
//
//
//        // Устанавливаем кнопки в разметку
//        markup.setKeyboard(rows);
//
//        // Добавляем клавиатуру в сообщение
//        message.setReplyMarkup(markup);
//
//        // Отправляем сообщение
//        try {
//            this.client.execute(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
