package ru.artemmaklashev.telegram_bot_raso.repositories.defects;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.defects.Defects;

import java.util.List;

public interface DefectsRepository extends JpaRepository<Defects, Integer> {

    List<Defects> findAllByDefectReasonIdAndDefectTypesId(int defectReasonId, int defectTypeId);
}
