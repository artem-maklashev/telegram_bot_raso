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
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.artemmaklashev.telegram_bot_raso.buttons.Button;
import ru.artemmaklashev.telegram_bot_raso.buttons.Buttons;
import ru.artemmaklashev.telegram_bot_raso.components.CallbackHandler;
import ru.artemmaklashev.telegram_bot_raso.components.CommandHandler;
import ru.artemmaklashev.telegram_bot_raso.components.Keyboards;
import ru.artemmaklashev.telegram_bot_raso.components.MessageService;
import ru.artemmaklashev.telegram_bot_raso.config.TelegramConfig;
import ru.artemmaklashev.telegram_bot_raso.controller.gypsumboard.GypsumBoardController;
import ru.artemmaklashev.telegram_bot_raso.model.TelegramUser;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.TelegramUserService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;


@Component
public class TelegramController {
    private final TelegramUserService userService;
    private final MessageService messageService;
    private final CommandHandler commandHandler;
    private final CallbackHandler callbackHandler;
    private final Keyboards keyboards;

    private Queue<TelegramUser> usersToApprove = new ArrayDeque<>();

    public TelegramController(TelegramUserService userService, MessageService messageService,
                              CommandHandler commandHandler, CallbackHandler callbackHandler, Keyboards keyboards) {
        this.userService = userService;
        this.messageService = messageService;
        this.commandHandler = commandHandler;
        this.callbackHandler = callbackHandler;
        this.keyboards = keyboards;
    }

    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleTextMessage(update);
        } else if (update.hasCallbackQuery()) {
            handleCallback(update);
        }
    }

    private void handleTextMessage(Update update) {
        var user = update.getMessage().getFrom();
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        if (userApproved(user)) {
            if (text.startsWith("/")) {
                // Обрабатываем команды через CommandHandler
                SendMessage commandResponse = commandHandler.handleCommand(chatId.toString(), text);
                messageService.sendMessage(commandResponse);
            } else {
                // Если это не команда, проверяем подтверждение пользователя
            }
        } else {
            messageService.sendMessage(SendMessage.builder().chatId(chatId).text(user.getFirstName() + ", Вы не зарегистрированы в боте!").build());
//            String message = String.format("Пользователь %s %s направил запрос на регистрацию", user.getFirstName(), user.getLastName());
//            InlineKeyboardMarkup adminKeyboard = keyboards.getApproveKeyboard();
//            TelegramUser newUser = new TelegramUser(user.getFirstName(), user.getLastName(), user.getUserName(), user.getId());
//            usersToApprove.add(newUser);
//            messageService.sendAdminMessage(message, adminKeyboard);
            userService.userApproveRequest(chatId);
        }
    }


    private void handleCallback(Update update) {
        Object response = callbackHandler.handleCallback(update);

        if (response instanceof BotApiMethod<?>) {
            messageService.executeMessage((BotApiMethod<?>) response);
        } else if (response instanceof SendPhoto) {
            messageService.sendImage((SendPhoto) response); // Отправка изображения
        }
    }


    private boolean userApproved(User user) {
        return userService.isKnownUser(user.getId()) && userService.isApproved(user.getId());
    }
}


