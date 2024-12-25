package ru.artemmaklashev.telegram_bot_raso.repositories.drymix;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays.MixUnit;

import java.util.List;

public interface MixUnitRepository extends JpaRepository<MixUnit, Long> {

    List<MixUnit> findAllByProductionAreaId(Long id);
}

