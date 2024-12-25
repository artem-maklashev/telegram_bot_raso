package ru.artemmaklashev.telegram_bot_raso.repositories.delays;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.DelayType;

public interface DelayTypeRepositiry extends JpaRepository<DelayType, Integer> {
}
