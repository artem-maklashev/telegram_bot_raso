package ru.artemmaklashev.telegram_bot_raso.repositories.drymix;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixPlan;

import java.time.LocalDate;
import java.util.List;

public interface MixPlanRepository extends JpaRepository<MixPlan, Long>{
    List<MixPlan> findByPlanDateBetween(LocalDate start, LocalDate end);
}
