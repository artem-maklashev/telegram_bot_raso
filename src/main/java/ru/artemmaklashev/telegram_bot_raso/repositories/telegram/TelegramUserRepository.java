package ru.artemmaklashev.telegram_bot_raso.repositories.telegram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.artemmaklashev.telegram_bot_raso.model.TelegramUser;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    boolean existsByTelegramId(Long userId);

    TelegramUser findByTelegramId(Long id);

    boolean existsByTelegramIdAndApproved(Long id, boolean b);
}
