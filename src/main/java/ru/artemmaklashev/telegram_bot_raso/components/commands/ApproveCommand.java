package ru.artemmaklashev.telegram_bot_raso.components.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.TelegramUserService;

@Component("approve")
public class ApproveCommand implements Command{

    private final TelegramUserService userService;

    public ApproveCommand(TelegramUserService userService) {
        this.userService = userService;
    }

    @Override
    public Object execute(Update update) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        var user = update.getCallbackQuery().getFrom();
        userService.approveUser(user, chatId);
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .text("Запрос отправлен")
                .build();
    }
}
