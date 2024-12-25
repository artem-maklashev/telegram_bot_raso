package ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;

public interface GypsumBoardRepository extends JpaRepository<GypsumBoard, Integer> {
}
