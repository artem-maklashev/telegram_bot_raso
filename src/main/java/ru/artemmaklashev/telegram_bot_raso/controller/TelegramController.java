package ru.artemmaklashev.telegram_bot_raso.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.artemmaklashev.telegram_bot_raso.buttons.Button;
import ru.artemmaklashev.telegram_bot_raso.buttons.Buttons;


@Component
public class TelegramController {
    @Value("${backend.api.url}")
    private String url;

    private final TelegramClient client;
    private final RestTemplate restTemplate;

    public TelegramController(TelegramClient client, RestTemplate restTemplate) {
        this.client = client;
        this.restTemplate = restTemplate;
    }

    /**
     * Метод для обработки входящего сообщения.
     */
    public void handleUpdate(Update update) {
        System.out.println("handleUpdate вызван. Тип обновления: " + (update.hasCallbackQuery() ? "CallbackQuery" : "Message"));

        if (update.hasMessage() && update.getMessage().hasText()) {
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

        if ("/start".equalsIgnoreCase(text)) {
            sendStartMessage(chatId);
        } else if ("/help".equalsIgnoreCase(text)) {
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
                editMessage(chatId, messageId, "Вы запросили отчет по ГСП.");
                // TODO: Отправить отчет
                String report = getReportData();
            } else {
                editMessage(chatId, messageId, "Неизвестное действие.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getReportData() {
        
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
    private void executeMessage(BotApiMethod<?> message) {
        try {
            this.client.execute(message);
            System.out.println("Сообщение отправлено: " + message);
        } catch (Exception e) {
            System.err.println("Ошибка при выполнении команды:");
            e.printStackTrace();
        }
    }



}

