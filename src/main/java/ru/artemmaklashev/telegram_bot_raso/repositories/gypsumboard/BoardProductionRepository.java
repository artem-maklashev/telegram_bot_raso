package ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.production.BoardProduction;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BoardProductionRepository extends JpaRepository<BoardProduction, Integer> {

//    List<BoardProduction> findAllByGypsumBoardIdIn(List<Integer> gypsumBoardIds);

    List<BoardProduction> findAllByProductionListIdIn(List<Long> ids);

    @Query("SELECT bp FROM BoardProduction bp WHERE bp.id IN (SELECT MIN(bp2.id) FROM BoardProduction bp2 WHERE bp2.productionList.id IN :productionLogIds GROUP BY bp2.productionList.id)")
    List<BoardProduction> findDistinctByProductionLogIdIn(@Param("productionLogIds") List<Long> productionLogIds);
    List<BoardProduction> findTopByProductionListIdInAndCategoryIdOrCategoryId(List<Long> productionLogIds, int firstCategoryId, int secondCategoryId);

    List<BoardProduction> findByProductionListId(long id);

    List<BoardProduction> findAllByProductionListIn(List<ProductionList> productionLists);

    BoardProduction findByProductionListAndCategoryId(ProductionList productionList, int categoryId);

     BoardProduction findBoardProductionById(Integer id);

    void deleteAllByProductionListId(Long id);

    List<BoardProduction> findBoardProductionsByProductIdAndCategoryIdAndProductionListProductionDateBetween(int productId, int categoryId, LocalDateTime startDate, LocalDateTime endDate);

    List<BoardProduction> findByProductAndProductionListProductionDateBetween(
            GypsumBoard product, LocalDateTime startDate, LocalDateTime endDate);
    List<BoardProduction> findByProductInAndProductionListProductionDateBetween(
            Collection<GypsumBoard> product, LocalDateTime productionList_productionDate, LocalDateTime productionList_productionDate2);

    List<BoardProduction> findByProductionListProductionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
