package ru.artemmaklashev.telegram_bot_raso.repositories.specification;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;
import ru.artemmaklashev.telegram_bot_raso.entity.specification.MaterialConsumption;

import java.time.LocalDateTime;
import java.util.List;

public interface MaterialConsumptionRepository extends JpaRepository<MaterialConsumption, Long> {
    List<MaterialConsumption> findByProductionListId(Long id);

    MaterialConsumption getConsumptionByProductionListId(long id);

     MaterialConsumption findFirstById(Long id);

    List<MaterialConsumption> findByMaterialIdAndProductionListProductionDateBetween(Long materialId, LocalDateTime startDateValue, LocalDateTime endDateValue);

    List<MaterialConsumption> findByProductionListIn(List<ProductionList> productionLists);
}
