package ru.artemmaklashev.telegram_bot_raso.service.telegram;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.artemmaklashev.telegram_bot_raso.buttons.Button;
import ru.artemmaklashev.telegram_bot_raso.buttons.Buttons;
import ru.artemmaklashev.telegram_bot_raso.components.Keyboards;
import ru.artemmaklashev.telegram_bot_raso.components.MessageService;
import ru.artemmaklashev.telegram_bot_raso.config.TelegramConfig;
import ru.artemmaklashev.telegram_bot_raso.model.TelegramUser;
import ru.artemmaklashev.telegram_bot_raso.repositories.telegram.TelegramUserRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;
    private final Keyboards keyboards;
    private final MessageService messageService;
    public final ConcurrentHashMap<String, CompletableFuture<Boolean>> pendingApprovals = new ConcurrentHashMap<>();

    public TelegramUserService(TelegramUserRepository telegramUserRepository, Keyboards keyboards, MessageService messageService) {
        this.telegramUserRepository = telegramUserRepository;
        this.keyboards = keyboards;
        this.messageService = messageService;
    }

    public boolean isKnownUser(Long userId) {
        return telegramUserRepository.existsByTelegramId(userId);
    }

    public void approveUser(User user, String chatId) {
        try {
            if (!isKnownUser(user.getId())) {
                TelegramUser telegramUser = new TelegramUser(user.getFirstName(), user.getLastName(), user.getUserName(), user.getId(), Long.parseLong(chatId));
                telegramUserRepository.save(telegramUser);
                System.out.println("Пользователь " + user.getFirstName() + " " + user.getLastName() + " добавлен  БД");
            }
            InlineKeyboardMarkup markup = keyboards.getApproveKeyboard(user);
            String messageText = user.getFirstName() + " " + user.getLastName() + " запросил доступ";
            messageService.sendAdminMessage(messageText, markup);// Создаем CompletableFuture для ожидания ответа
//            CompletableFuture<Boolean> future = new CompletableFuture<>();
//            pendingApprovals.put(chatId, future);

            // Ждем ответа
//            return future.get();
        } catch (Exception e) {
            e.printStackTrace();
//            return false;
        }
    }

    // Метод для обработки callback-запросов
    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String data = callbackQuery.getData();

//        CompletableFuture<Boolean> future = pendingApprovals.get(chatId);
//        if (future != null) {
            if (data.startsWith("authorize_")) {
                long userId = Long.parseLong(data.substring("authorize_".length())); // Получаем userId
//                future.complete(true);
                messageService.sendAdminMessage("✅ Доступ одобрен пользователю с ID: " + userId);
                TelegramUser telegramUser = telegramUserRepository.findByTelegramId(userId);
                if (telegramUser != null) {
                    telegramUser.setApproved(true);
                    telegramUserRepository.save(telegramUser);
                    System.out.println("�� Доступ одобрен пользователю с ID: " + userId);
                } else {
                    System.out.println("⚠ Ошибка: пользователь с ID " + userId + " не найден в БД.");
                }
            } else if (data.startsWith("deny_")) {
                long userId = Long.parseLong(data.substring("deny_".length())); // Получаем userId
//                future.complete(false);
                messageService.sendAdminMessage("⛔ Доступ отклонен пользователю с ID: " + userId);
            }
//            pendingApprovals.remove(chatId);
//        }
    }

    public boolean isApproved(Long id) {
        return telegramUserRepository.existsByTelegramIdAndApproved(id, true);
    }

    public void userApproveRequest(Long chatId) {
        InlineKeyboardMarkup markup = keyboards.getUserApproveKeyboard(); // Создаем InlineKeyboardMarkup;
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(markup)
                .text("Вы хотите получить доступ к боту?")
                .build();
        messageService.sendMessage(sendMessage);
    }

    public void sendMessage(String chatId, String s) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(s)
                .parseMode("MarkdownV2")
                .build();
        messageService.sendMessage(sendMessage);
    }
}

