package ru.artemmaklashev.telegram_bot_raso.repositories.delays;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.Unit;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Integer> {
    List<Integer> findAllByProductionAreaIdIn(List<Integer> ids);

    List<Unit> findAllByProductionAreaId(int id);
}
