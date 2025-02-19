package ru.artemmaklashev.telegram_bot_raso.components;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artemmaklashev.telegram_bot_raso.controller.gypsumboard.GypsumBoardController;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.TelegramUserService;

@Component
public class CallbackHandler {
    private final GypsumBoardController gypsumBoardController;
    private final TelegramUserService userService;

    public CallbackHandler(GypsumBoardController gypsumBoardController, TelegramUserService userService) {
        this.gypsumBoardController = gypsumBoardController;
        this.userService = userService;
    }

    public BotApiMethod<?> handleCallback(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();

        if ("gypsumBoardReport".equalsIgnoreCase(callbackData)) {
            return handleGypsumBoardReport(chatId, messageId);
        } else if ("approve".equalsIgnoreCase(callbackData)) {
            return handleApprove(update, chatId);
        } else {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("Неизвестное действие.")
                    .build();
        }
    }

    private EditMessageText handleGypsumBoardReport(String chatId, int messageId) {
        String report = gypsumBoardController.getReportData();
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Вы запросили отчет по ГСП:\n" + report)
                .build();
    }

    private EditMessageText handleApprove(Update update, String chatId) {
        var user = update.getCallbackQuery().getFrom();
        boolean approved = userService.approveUser(user, chatId, null);
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .text(approved ? "Вы успешно подтверждены!" : "Ошибка подтверждения.")
                .build();
    }
}

