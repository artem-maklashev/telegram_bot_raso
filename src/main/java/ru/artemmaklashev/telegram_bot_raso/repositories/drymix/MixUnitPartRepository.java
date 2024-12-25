package ru.artemmaklashev.telegram_bot_raso.repositories.drymix;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays.MixUnitPart;

import java.util.List;

public interface MixUnitPartRepository extends JpaRepository<MixUnitPart, Long> {
    List<MixUnitPart> findAllByUnitIdAndDelayTypeId(Long unit_id, int delayType_id);


}
