package ru.artemmaklashev.telegram_bot_raso.components;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.artemmaklashev.telegram_bot_raso.buttons.Button;
import ru.artemmaklashev.telegram_bot_raso.buttons.Buttons;

@Component
public class Keyboards {
    public InlineKeyboardMarkup getReportKeyboard() {
        Button gypsumBoard = new Button("Отчет по ГСП", "gypsumBoardReport");
        Button dryMix = new Button("Отчет по сухим смесям", "dryMixReport");
        Buttons buttons = new Buttons();
        buttons.addButton(gypsumBoard, 1);
        buttons.addButton(dryMix, 2);
        InlineKeyboardMarkup markup = buttons.build();
        System.out.println("Клавиатура создана успешно " + markup);
        return markup;
    }

    public InlineKeyboardMarkup getApproveKeyboard(User user) {
        String userId = String.valueOf(user.getId()); // Получаем ID пользователя Telegram

        Button approve = new Button("✅ Подтвердить", "authorize_" + userId );
        Button deny = new Button("⛔ Отклонить", "deny_" + userId);

        Buttons buttons = new Buttons();
        buttons.addButton(approve, 1);
        buttons.addButton(deny, 1);

        InlineKeyboardMarkup markup = buttons.build();
        System.out.println("Клавиатура создана успешно: " + markup);
        return markup;
    }

    public InlineKeyboardMarkup getUserApproveKeyboard() {
        Button approve = new Button("✅ Запросить доступ", "approve");


        Buttons buttons = new Buttons();
        buttons.addButton(approve, 1);

        InlineKeyboardMarkup markup = buttons.build();
        System.out.println("Клавиатура запроса доступа создана успешно: " + markup);
        return markup;
    }

}
