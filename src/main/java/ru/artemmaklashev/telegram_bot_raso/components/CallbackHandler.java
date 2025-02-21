package ru.artemmaklashev.telegram_bot_raso.components;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artemmaklashev.telegram_bot_raso.controller.drymix.DryMixController;
import ru.artemmaklashev.telegram_bot_raso.controller.gypsumboard.GypsumBoardController;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.TelegramUserService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class CallbackHandler {
    private final GypsumBoardController gypsumBoardController;
    private final DryMixController dryMixController;
    private final TelegramUserService userService;

    public CallbackHandler(GypsumBoardController gypsumBoardController, DryMixController dryMixController, TelegramUserService userService) {
        this.gypsumBoardController = gypsumBoardController;
        this.dryMixController = dryMixController;
        this.userService = userService;
    }

    public Object handleCallback(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();

        if ("gypsumBoardReport".equalsIgnoreCase(callbackData)) {
            return handleGypsumBoardReport(chatId, messageId);
        } else if (callbackData.startsWith("approve")) {
            return handleApprove(update, chatId);
        } else if ("dryMixReport".equalsIgnoreCase(callbackData)) {
            return handleDryMixReport(chatId, messageId);
        } else {
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("Неизвестное действие.")
                    .build();
        }
    }

    private Object handleDryMixReport(String chatId, int messageId) {

        String report = dryMixController.getDelaysData();
        BufferedImage image = dryMixController.getImageReport();
        return draw(report, image, chatId, messageId);
    }

    public Object handleGypsumBoardReport(String chatId, int messageId) {
        // Получаем отчет и изображение
        String report = gypsumBoardController.getReportData();
        BufferedImage image = gypsumBoardController.getImageReport();
        return draw(report, image, chatId, messageId);
    }

    private EditMessageText handleApprove(Update update, String chatId) {
        var user = update.getCallbackQuery().getFrom();
        userService.approveUser(user, chatId);
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .text("Запрос отправлен")
                .build();
    }

    private Object draw(String report, BufferedImage image, String chatId, int messageId) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            InputStream is = new ByteArrayInputStream(os.toByteArray());

            // Создаем InputFile для отправки изображения
            InputFile photo = new InputFile(is, "report.png");
            String header = "Выпуск продукции за " + LocalDate.now().minusDays(1L)
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("ru")));
            String escapedText = header.replace("*", "\\*")
                    .replace("_", "\\_")
                    .replace("[", "\\[")
                    .replace("]", "\\]")
                    .replace("(", "\\(")
                    .replace(")", "\\)")
                    .replace("~", "\\~")
                    .replace("`", "\\`")
                    .replace(">", "\\>")
                    .replace("#", "\\#")
                    .replace("+", "\\+")
                    .replace("-", "\\-")
                    .replace("=", "\\=")
                    .replace("|", "\\|")
                    .replace("{", "\\{")
                    .replace("}", "\\}")
                    .replace(".", "\\.")
                    .replace("!", "\\!");
            userService.sendMessage(chatId, "*" + escapedText + "*");
            // Создаем сообщение с изображением и подписью
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(photo)
                    .caption(report) // Подпись под изображением
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
}

