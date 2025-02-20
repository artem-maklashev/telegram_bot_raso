package ru.artemmaklashev.telegram_bot_raso.components;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artemmaklashev.telegram_bot_raso.controller.gypsumboard.GypsumBoardController;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.TelegramUserService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Component
public class CallbackHandler {
    private final GypsumBoardController gypsumBoardController;
    private final TelegramUserService userService;

    public CallbackHandler(GypsumBoardController gypsumBoardController, TelegramUserService userService) {
        this.gypsumBoardController = gypsumBoardController;
        this.userService = userService;
    }

    public Object handleCallback(Update update) {
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

    public Object handleGypsumBoardReport(String chatId, int messageId) {
        try {
            // Получаем отчет и изображение
            String report = gypsumBoardController.getReportData();
            BufferedImage image = gypsumBoardController.getImageReport();

            // Конвертируем BufferedImage в InputStream
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());

            // Создаем InputFile для отправки изображения
            InputFile photo = new InputFile(is, "report.png");

            // Создаем сообщение с изображением и подписью
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(photo)
                    .caption("Вы запросили отчет по ГСП:\n" + report) // Подпись под изображением
                    .build();

            // Закрываем ресурсы
            os.close();
            is.close();

            return sendPhoto;
        } catch (Exception e) {
            e.printStackTrace();
            // В случае ошибки возвращаем текстовое сообщение об ошибке
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("Произошла ошибка при формировании отчета.")
                    .build();
        }
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

