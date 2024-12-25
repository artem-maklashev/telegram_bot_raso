package ru.artemmaklashev.telegram_bot_raso.repositories.drymix;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixCategoryProduction;

import java.time.LocalDateTime;
import java.util.List;

public interface MixCategoryProductionRepository extends JpaRepository<MixCategoryProduction, Long> {
    List<MixCategoryProduction> findAllByProductionProductionDateBetween(LocalDateTime startDateLocal, LocalDateTime endDateLocal);

    List<MixCategoryProduction> findAllByProductionIdIn(List<Long> productionsIds);

    List<MixCategoryProduction> findAllByProductionId(Long productionId);

    List<MixCategoryProduction> findByProductionProductionDateBetween(LocalDateTime startDateLocal, LocalDateTime endDateLocal);

}
