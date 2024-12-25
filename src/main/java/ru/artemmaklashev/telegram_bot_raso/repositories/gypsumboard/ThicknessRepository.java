package ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.Thickness;

public interface ThicknessRepository extends JpaRepository<Thickness, Integer> {
}
