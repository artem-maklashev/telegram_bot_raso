package ru.artemmaklashev.telegram_bot_raso.repositories.drymix;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays.MixDelay;

import java.time.LocalDateTime;
import java.util.List;

public interface MixDelayRepository extends JpaRepository<MixDelay, Long> {

    List<MixDelay> findByMixProductionId(Long productionId);

    List<MixDelay> findAllByMixProductionProductionDateBetween(LocalDateTime start, LocalDateTime end);
}
