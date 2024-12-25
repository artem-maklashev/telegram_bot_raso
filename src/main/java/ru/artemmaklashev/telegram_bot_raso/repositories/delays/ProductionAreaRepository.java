package ru.artemmaklashev.telegram_bot_raso.repositories.delays;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.ProductionArea;

import java.util.List;

public interface ProductionAreaRepository extends JpaRepository<ProductionArea, Integer> {
    List<Integer> findByDivisionId(int id);

    List<ProductionArea> findAllByDivisionId(int division_id);
}
