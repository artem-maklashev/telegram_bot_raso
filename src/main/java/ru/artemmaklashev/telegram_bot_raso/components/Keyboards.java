package ru.artemmaklashev.telegram_bot_raso.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.artemmaklashev.telegram_bot_raso.buttons.Button;
import ru.artemmaklashev.telegram_bot_raso.buttons.Buttons;
@Component
public class Keyboards {
    public InlineKeyboardMarkup getReportKeyboard() {
            Button button = new Button("Отчет по ГСП", "gypsumBoardReport");
            Buttons buttons = new Buttons();
            buttons.addButton(button, 1);
            InlineKeyboardMarkup markup = buttons.build();
            System.out.println("Клавиатура создана успешно " + markup);
            return markup;
    }
}
