package ru.artemmaklashev.telegram_bot_raso.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.artemmaklashev.telegram_bot_raso.buttons.Button;
import ru.artemmaklashev.telegram_bot_raso.buttons.Buttons;
import ru.artemmaklashev.telegram_bot_raso.charts.SampleCgart;
import ru.artemmaklashev.telegram_bot_raso.controller.gypsumboard.GypsumBoardController;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.BoardDelays;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.Delays;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.production.BoardProduction;
import ru.artemmaklashev.telegram_bot_raso.service.report.ASCIItable;
import ru.artemmaklashev.telegram_bot_raso.service.report.ASCIItableImage;
import ru.artemmaklashev.telegram_bot_raso.service.reportServices.gypsumBoard.GypsymBoardReportService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class TelegramController {
    @Value("${backend.api.url}")
    private String url;

    private final TelegramClient client;
    private final RestTemplate restTemplate;
//    private final GypsymBoardReportService gypsymBoardReportService;
    private final GypsumBoardController gypsumBoardController;

    public TelegramController(TelegramClient client, RestTemplate restTemplate,  GypsumBoardController gypsumBoardController) {
        this.client = client;
        this.restTemplate = restTemplate;
        this.gypsumBoardController = gypsumBoardController;
    }

    /**
     * Метод для обработки входящего сообщения.
     */
    public void handleUpdate(Update update) {
        System.out.println("handleUpdate вызван. Тип обновления: " + (update.hasCallbackQuery() ? "CallbackQuery" : "Message"));

        if (update.hasMessage() && update.getMessage().hasText()) {
            var user = update.getMessage().getFrom();
            handleTextCommand(update);
        } else if (update.hasCallbackQuery()) {
            handleCallback(update);
        } else {
            System.out.println("Неизвестный тип обновления");
        }
    }


    /**
     * Обработка текстовых команд.
     */
    private void handleTextCommand(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String text = update.getMessage().getText();

        if ("/start".equalsIgnoreCase(text) || "/start@raso_helper_bot".equalsIgnoreCase(text) ) {
            sendStartMessage(chatId);
        } else if ("/help".equalsIgnoreCase(text) || "/help@raso_helper_bot".equalsIgnoreCase(text)) {
            sendHelpMessage(chatId);
        } else {
            sendUnknownCommandMessage(chatId);
        }
    }

    /**
     * Обработка callback-запросов.
     */
    private void handleCallback(Update update) {
        System.out.println("Обработка команды меню:");
        try {
            String callbackData = update.getCallbackQuery().getData();
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            System.out.println("Получено callbackData: " + callbackData);


            if ("gypsumBoardReport".equalsIgnoreCase(callbackData)) {
                editMessage(chatId, messageId, update.getCallbackQuery().getFrom().getFirstName() + ", Вы запросили отчет по ГСП.");
                // TODO: Отправить отчет
                String report = gypsumBoardController.getReportData();
                sendImage(chatId, gypsumBoardController.getImageReport(),
                        ("*Выпуск продукции за " + LocalDate.now().minusDays(1L).format(DateTimeFormatter.ISO_LOCAL_DATE)+"*\n")
                                .replace("-", "\\-"));
                sendNewMessage(chatId, report);
            } else {
                editMessage(chatId, messageId, "Неизвестное действие.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Отправка сообщения при команде /start.
     */
    private void sendStartMessage(String chatId) {
        String messageText = "Добро пожаловать! Используйте /help для списка доступных команд.";
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .replyMarkup(createInlineKeyboard(chatId, client))
                .build();

        executeMessage(message);
    }

    /**
     * Отправка сообщения при команде /help.
     */
    private void sendHelpMessage(String chatId) {
        String messageText = "Список команд:\n/start - начать работу\n/help - помощь";
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();

        executeMessage(message);
    }

    /**
     * Отправка сообщения для неизвестных команд.
     */
    private void sendUnknownCommandMessage(String chatId) {
        String messageText = "Неизвестная команда. Используйте /help для списка доступных команд.";
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();

        executeMessage(message);
    }

    /**
     * Редактирование сообщения (для обработки callback).
     */
    private void editMessage(String chatId, int messageId, String newText) {
        System.out.println("Редактирование сообщения:");
        System.out.println("chatId: " + chatId);
        System.out.println("messageId: " + messageId);
        System.out.println("newText: " + newText);
        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(newText)
                .build();

        executeMessage(editMessage);
    }

    /**
     * Создание клавиатуры с кнопками.
     */
    private InlineKeyboardMarkup createInlineKeyboard(String chatId, TelegramClient telegramClient) {
        try {
            Long longChatId = Long.parseLong(chatId);
            Button button = new Button("Отчет по ГСП", "gypsumBoardReport");
            Buttons buttons = new Buttons(longChatId, telegramClient);
            buttons.addButton(button, 1);
            InlineKeyboardMarkup markup = buttons.build();
            System.out.println("Клавиатура создана успешно");
            return markup;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка преобразования chatId: " + e.getMessage());
        }
        return null;
    }

    /**
     * Выполнение команды Telegram API.
     */
    public void executeMessage(BotApiMethod<?> message) {
        try {
            this.client.execute(message);
            System.out.println("Сообщение отправлено: " + message);
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении команды:");
            e.printStackTrace();
        }
    }

    private void sendNewMessage(String chatId, String newMessage) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(newMessage)
                .parseMode("HTML")
//                .parseMode("MarkdownV2")
                .build();

        executeMessage(message);
    }

    public void sendImage(String chatId, BufferedImage image, String caption) {
        try {
            // Конвертируем BufferedImage в InputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos); // Сохраняем в формате PNG
            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());

            // Создаем объект InputFile
            InputFile photoFile = new InputFile(inputStream, "chart.png");

            // Создаем SendPhoto
            SendPhoto message = SendPhoto.builder()
                    .chatId(chatId)
                    .photo(photoFile)
                    .caption(caption)
                    .showCaptionAboveMedia(true)
                    .parseMode("MarkdownV2")
                    .build();

            // Отправьте сообщение с помощью метода execute (зависит от реализации вашего бота)
            // Например:
            // bot.execute(message);
            executeImage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeImage(SendPhoto message) {
        try {
            this.client.execute(message);
            System.out.println("Сообщение отправлено: " + message);
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении команды:");
            e.printStackTrace();
        }
    }



}

