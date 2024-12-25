package ru.artemmaklashev.telegram_bot_raso.repositories.delays;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.UnitPart;

import java.util.List;

public interface UnitPartRepository extends JpaRepository<UnitPart, Integer> {
    List<Integer> findAllByUnitIdIn(List<Integer> ids);

    List<UnitPart> findAllByUnitId(int id);
}
