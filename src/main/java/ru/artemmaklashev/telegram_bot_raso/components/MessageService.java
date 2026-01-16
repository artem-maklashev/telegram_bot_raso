package ru.artemmaklashev.telegram_bot_raso.components;

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
public class MessageService {
    private final TelegramClient client;
    private final TelegramConfig config;

    public MessageService(TelegramClient client, TelegramConfig config) {
        this.client = client;
        this.config = config;
    }

    public void sendMessage(SendMessage message) {
//        SendMessage message = SendMessage.builder()
//                .chatId(chatId)
//                .text(text)
//                .parseMode("HTML")
//                .build();
        executeMessage(message);
    }

    public void sendImage(SendPhoto photo) {

//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(image, "png", baos);
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
//            InputFile photoFile = new InputFile(inputStream, "chart.png");
//
//            SendPhoto message = SendPhoto.builder()
//                    .chatId(chatId)
//                    .photo(photoFile)
//                    .caption(caption)
//                    .parseMode("MarkdownV2")
//                    .build();

        executeImage(photo);

    }

    public void executeMessage(BotApiMethod<?> message) {
        try {
            client.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void executeImage(SendPhoto message) {
        try {
            client.execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAdminMessage(String text, InlineKeyboardMarkup keyboard) {
        List<String> chatIds = config.getNotification().getChatIds();
        for (String chatId : chatIds) {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .replyMarkup(keyboard)
                    .parseMode("HTML")
                    .build();
            executeMessage(message);
        }
    }

    public void sendAdminMessage(String text) {
        List<String> chatIds = config.getNotification().getChatIds();
        for (String chatId : chatIds) {
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(text)
                    .parseMode("HTML")
                    .build();
            executeMessage(message);
        }
    }

    public void sendDocument(SendDocument response) {
        try {
            client.execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void answerCallbackQuery(AnswerCallbackQuery build) {
        executeMessage(build);
    }
}

