package ru.artemmaklashev.telegram_bot_raso.components;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import ru.artemmaklashev.telegram_bot_raso.components.commands.Command;
import ru.artemmaklashev.telegram_bot_raso.controller.drymix.DryMixController;
import ru.artemmaklashev.telegram_bot_raso.controller.gypsumboard.GypsumBoardController;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.GypsumBoardProductionData;
import ru.artemmaklashev.telegram_bot_raso.service.html.HtmlProductionTable;
import ru.artemmaklashev.telegram_bot_raso.service.html.HtmlToImageConverter;
import ru.artemmaklashev.telegram_bot_raso.service.reportServices.gypsumBoard.GypsymBoardReportService;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.TelegramUserService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class CallbackHandler {

    private final CallbackCommandRegistry registry;


    public CallbackHandler(CallbackCommandRegistry registry) {

        this.registry = registry;
    }

    public List<Object> handleCallback(Update update) throws IOException {

        var callback = update.getCallbackQuery();
        String callbackData = callback.getData();

        List<Object> responses = new ArrayList<>();

        // 1️⃣ ВСЕГДА отвечаем на callback
        responses.add(
                AnswerCallbackQuery.builder()
                        .callbackQueryId(callback.getId())
                        .build()
        );

        Command command = registry.getCommand(callbackData);

        if (command != null) {
            Object result = command.execute(update);

            if (result != null) {
                responses.add(result);
            }

        } else {
            Long chatId = callback.getMessage().getChatId();
            Integer messageId = callback.getMessage().getMessageId();

            responses.add(
                    EditMessageText.builder()
                            .chatId(chatId)
                            .messageId(messageId)
                            .text("Неизвестное действие.")
                            .build()
            );
        }

        return responses;
    }


//    private Object handleGypsumBoardTable(String chatId, int messageId)  {
//        List<GypsumBoardProductionData> productionData = gypsymBoardReportService.getProductionsByInterval();
//        if (!productionData.isEmpty()) {
//            HtmlProductionTable htmlProductionTable = new HtmlProductionTable(templateEngine);
//            String html = htmlProductionTable.render(productionData);
//            byte[] bytes ;
//            try {
//                bytes = htmlToImageConverter.convertHtmlToImage(html);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
////            return SendMessage.builder()
////                    .chatId(chatId)
////                    .text("отчет сгенерирован")
////                    .build();
//            if (bytes != null) {
//                return SendPhoto.builder()
//                        .chatId(chatId)
//                        .photo(new InputFile(new ByteArrayInputStream(bytes), "report.png"))
//                        .caption("Отчет сгенерирован")
//                        .build();
//            } else {
//                return EditMessageText.builder()
//                        .chatId(chatId)
//                        .messageId(messageId)
//                        .text("Произошла ошибка при формировании отчета.")
//                        .build();
//            }
//
//        } else {
//            return SendMessage.builder()
//                    .chatId(chatId)
//                    .text("Нет данных")
//                    .build();
//        }
//    }

//    private Object handleDryMixReport(String chatId, int messageId) {
//
//        String report = dryMixController.getDelaysData();
//        BufferedImage image = dryMixController.getImageReport();
//        return draw(report, image, chatId, messageId);
//    }

//    public Object handleGypsumBoardReport(String chatId, int messageId) {
//        // Получаем отчет и изображение
//        String report = gypsumBoardController.getReportData();
//        BufferedImage image = gypsumBoardController.getImageReport();
//        return draw(report, image, chatId, messageId);
//    }

//    private EditMessageText handleApprove(Update update, String chatId) {
//        var user = update.getCallbackQuery().getFrom();
//        userService.approveUser(user, chatId);
//        return EditMessageText.builder()
//                .chatId(chatId)
//                .messageId(update.getCallbackQuery().getMessage().getMessageId())
//                .text("Запрос отправлен")
//                .build();
//    }

//    private Object draw(String report, BufferedImage image, String chatId, int messageId) {
//        try {
//            ByteArrayOutputStream os = new ByteArrayOutputStream();
//            ImageIO.write(image, "png", os);
//            InputStream is = new ByteArrayInputStream(os.toByteArray());
//
//            // Создаем InputFile для отправки изображения
//            InputFile photo = new InputFile(is, "report.png");
//            String header = "Выпуск продукции за " + LocalDate.now().minusDays(1L)
//                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("ru")));
//            String escapedText = header.replace("*", "\\*")
//                    .replace("_", "\\_")
//                    .replace("[", "\\[")
//                    .replace("]", "\\]")
//                    .replace("(", "\\(")
//                    .replace(")", "\\)")
//                    .replace("~", "\\~")
//                    .replace("`", "\\`")
//                    .replace(">", "\\>")
//                    .replace("#", "\\#")
//                    .replace("+", "\\+")
//                    .replace("-", "\\-")
//                    .replace("=", "\\=")
//                    .replace("|", "\\|")
//                    .replace("{", "\\{")
//                    .replace("}", "\\}")
//                    .replace(".", "\\.")
//                    .replace("!", "\\!");
//            userService.sendMessage(chatId, "*" + escapedText + "*");
//            // Создаем сообщение с изображением и подписью
//            SendPhoto sendPhoto = SendPhoto.builder()
//                    .chatId(chatId)
//                    .photo(photo)
//                    .caption(report) // Подпись под изображением
//                    .build();
//
//            // Закрываем ресурсы
//            os.close();
//            is.close();
//
//            return sendPhoto;
//        } catch (Exception e) {
//            e.printStackTrace();
//            // В случае ошибки возвращаем текстовое сообщение об ошибке
//            return EditMessageText.builder()
//                    .chatId(chatId)
//                    .messageId(messageId)
//                    .text("Произошла ошибка при формировании отчета.")
//                    .build();
//        }
//    }
}

