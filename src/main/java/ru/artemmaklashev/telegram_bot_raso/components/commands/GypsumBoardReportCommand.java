package ru.artemmaklashev.telegram_bot_raso.components.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artemmaklashev.telegram_bot_raso.components.MessageService;
import ru.artemmaklashev.telegram_bot_raso.controller.gypsumboard.GypsumBoardController;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.RequestLoggingService;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.TelegramUserService;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

@Component("gypsumBoardReport")
public class GypsumBoardReportCommand implements Command {

    private final GypsumBoardController controller;
    private final TelegramUserService userService;
    private final MessageService messageService;
    private final RequestLoggingService loggingService;

    public GypsumBoardReportCommand(GypsumBoardController controller, TelegramUserService userService, MessageService messageService, RequestLoggingService loggingService) {
        this.controller = controller;
        this.userService = userService;
        this.messageService = messageService;
        this.loggingService = loggingService;
    }


    @Override
    public Object execute(Update update) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        String userName = update.getCallbackQuery().getFrom().getFirstName() + " "  + update.getCallbackQuery().getFrom().getLastName();
        logRequest(update.getCallbackQuery().getFrom().getId(), Long.parseLong(chatId), userName, "gypsumBoardReport");
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        String report = controller.getReportData();
        BufferedImage image = controller.getImageReport();
        sendAdminMessage("Отчет за сутки по гипсокартону для  " + userName + " готов");
        return userService.draw(report, image, chatId, messageId);
    }

    @Override
    public void logRequest(Long userId, Long chatId, String text, String command) {
        CompletableFuture.runAsync(() -> loggingService.logRequest(userId, chatId, text, command));
    }

    @Override
    public void sendAdminMessage(String text) {
        messageService.sendAdminMessage(text);
    }


}
