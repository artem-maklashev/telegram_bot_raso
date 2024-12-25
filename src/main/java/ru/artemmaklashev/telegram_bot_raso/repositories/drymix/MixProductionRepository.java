package ru.artemmaklashev.telegram_bot_raso.repositories.drymix;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixProduction;

import java.time.LocalDateTime;
import java.util.List;

public interface MixProductionRepository extends JpaRepository<MixProduction, Long> {

    List<MixProduction> findTop10ByOrderByProductionDateDesc();


    List<MixProduction> findAllByProductionDateBetween(LocalDateTime start, LocalDateTime end);
}
