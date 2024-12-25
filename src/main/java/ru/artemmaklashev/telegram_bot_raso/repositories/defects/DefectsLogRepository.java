package ru.artemmaklashev.telegram_bot_raso.repositories.defects;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.BoardDefectsLog;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;

import java.util.List;

public interface DefectsLogRepository extends JpaRepository<BoardDefectsLog, Long> {
    List<BoardDefectsLog> findBoardDefectsLogsByProductionListIdIn(List<Long> productionLogIds);
    List<BoardDefectsLog> findBoardDefectsLogsByProductionListIn(List<ProductionList> productionLists);

    List<BoardDefectsLog> findByProductionListId(long id);
    void deleteAllByProductionListId(long id);
}
