package ru.artemmaklashev.telegram_bot_raso.service.telegram;

import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.repositories.telegram.TelegramUserRepository;

@Service
public class TelegramUserService {
    private final TelegramUserRepository telegramUserRepository;

    public TelegramUserService(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    public boolean isKnownUser(Long userId) {
        return telegramUserRepository.existsByTelegramId(userId);
    }
}

