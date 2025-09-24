package ru.artemmaklashev.telegram_bot_raso.components.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artemmaklashev.telegram_bot_raso.controller.drymix.DryMixController;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.TelegramUserService;

import java.awt.image.BufferedImage;
@Component("dryMixReport")
public class DryMixReportCommand implements Command{

    private final DryMixController controller;
    private final TelegramUserService userService;

    public DryMixReportCommand(DryMixController controller, TelegramUserService userService) {
        this.controller = controller;
        this.userService = userService;
    }

    @Override
    public Object execute(Update update) {
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        String report = controller.getDelaysData();
        BufferedImage image = controller.getImageReport();
        return userService.draw(report, image, chatId, messageId);
    }
}
