package ru.artemmaklashev.telegram_bot_raso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.DelayType;

public interface DelayTypeRepository extends JpaRepository<DelayType, Integer> {
}
