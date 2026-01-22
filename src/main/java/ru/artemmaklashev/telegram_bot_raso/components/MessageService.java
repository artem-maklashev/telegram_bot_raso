package ru.artemmaklashev.telegram_bot_raso.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import ru.artemmaklashev.telegram_bot_raso.config.TelegramConfig;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class MessageService {
    private final TelegramClient client;
    private final TelegramConfig config;

    public MessageService(TelegramClient client, TelegramConfig config) {
        this.client = client;
        this.config = config;
    }

    public void sendMessage(SendMessage message) {
        executeRequest(message);
    }

    public void sendImage(SendPhoto photo) {
        executeRequest(photo);
    }

    public void sendDocument(SendDocument document) {
        executeRequest(document);
    }

    public void answerCallbackQuery(AnswerCallbackQuery callbackQuery) {
        executeRequest(callbackQuery);
    }

    public void sendAdminMessage(String text, InlineKeyboardMarkup keyboard) {
        sendToAllAdmins(text, keyboard);
    }

    public void sendAdminMessage(String text) {
        sendToAllAdmins(text, null);
    }

    public void executeRequest(BotApiMethod<?> method) {
        try {
            client.execute(method);
        } catch (Exception e) {
            log.error("Failed to execute Telegram API method: {}", method.getClass().getSimpleName(), e);
        }
    }

    private void executeRequest(SendPhoto photo) {
        try {
            client.execute(photo);
        } catch (Exception e) {
            log.error("Failed to send photo", e);
        }
    }

    private void executeRequest(SendDocument document) {
        try {
            client.execute(document);
        } catch (Exception e) {
            log.error("Failed to send document", e);
        }
    }

    private void sendToAllAdmins(String text, InlineKeyboardMarkup keyboard) {
        List<String> adminChatIds = config.getNotification().getChatIds();

        if (adminChatIds == null || adminChatIds.isEmpty()) {
            log.warn("No admin chat IDs configured");
            return;
        }

        adminChatIds.forEach(chatId -> sendAdminMessage(chatId, text, keyboard));
    }

    private void sendAdminMessage(String chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage.SendMessageBuilder messageBuilder = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .parseMode("HTML");

        if (keyboard != null) {
            messageBuilder.replyMarkup(keyboard);
        }

        executeRequest(messageBuilder.build());
    }
}


