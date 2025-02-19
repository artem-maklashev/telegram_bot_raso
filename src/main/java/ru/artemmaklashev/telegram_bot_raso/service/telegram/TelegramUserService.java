package ru.artemmaklashev.telegram_bot_raso.service.telegram;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.artemmaklashev.telegram_bot_raso.buttons.Button;
import ru.artemmaklashev.telegram_bot_raso.buttons.Buttons;
import ru.artemmaklashev.telegram_bot_raso.controller.TelegramUserController;
import ru.artemmaklashev.telegram_bot_raso.repositories.telegram.TelegramUserRepository;
import ru.artemmaklashev.telegram_bot_raso.service.gypsumboard.TelegramNotificationSender;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;
    private final TelegramNotificationSender telegramNotificationSender;

    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> pendingApprovals = new ConcurrentHashMap<>();

    public TelegramUserService(TelegramUserRepository telegramUserRepository, TelegramUserController telegramUserController, TelegramNotificationSender telegramNotificationSender) {
        this.telegramUserRepository = telegramUserRepository;
        this.telegramNotificationSender = telegramNotificationSender;
    }

    public boolean isKnownUser(Long userId) {
        return telegramUserRepository.existsByTelegramId(userId);
    }

    public InlineKeyboardMarkup approvedRequest(Long id, TelegramClient client) {
        try {
            Button button = new Button("Запросить доступ", "approve");
            Buttons buttons = new Buttons();
            buttons.addButton(button, 1);
            InlineKeyboardMarkup markup = buttons.build();
            System.out.println("Клавиатура запроса доступа успешно создана");
            return markup;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка преобразования chatId: " + e.getMessage());
        }
        return null;
    }

    public Object approveUser(User user, String chatId, TelegramClient client) {
        try {
            Button authorize = new Button("Разрешить", "authorize");
            Button deny = new Button("Отклонить", "deny");
            Buttons buttons = new Buttons();
            buttons.addButton(authorize, 1);
            buttons.addButton(deny, 1);
            InlineKeyboardMarkup markup = buttons.build();
            System.out.println("Клавиатура авторизации пользователя создана успешно");
            String messageText = user.getFirstName() + " " + user.getLastName() + " запросил доступ";
            SendMessage message = SendMessage.builder()
                    .chatId(chatId)
                    .text(messageText)
                    .replyMarkup(markup)
                    .build();
            telegramNotificationSender.sendMessage(message);// Создаем CompletableFuture для ожидания ответа
            CompletableFuture<Boolean> future = new CompletableFuture<>();
            pendingApprovals.put(chatId, future);

            // Ждем ответа
            return future.get(); // Блокирует поток, пока не получит ответ
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Метод для обработки callback-запросов
    public void handleCallbackQuery(CallbackQuery callbackQuery) {
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String data = callbackQuery.getData(); // "authorize" или "deny"

        CompletableFuture<Boolean> future = pendingApprovals.get(chatId);
        if (future != null) {
            if ("authorize".equals(data)) {
                future.complete(true); // Пользователь разрешил
            } else if ("deny".equals(data)) {
                future.complete(false); // Пользователь отклонил
            }
            pendingApprovals.remove(chatId); // Удаляем из ожидания
        }
    }
}

