package ru.artemmaklashev.telegram_bot_raso.service.gypsumboard;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;
import ru.artemmaklashev.telegram_bot_raso.entity.specification.MaterialConsumption;
import ru.artemmaklashev.telegram_bot_raso.repositories.specification.MaterialConsumptionRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class MaterialConsumptionService {

    @Value("${spring.jackson.time-zone}")
    private String timeZone;


    private final MaterialConsumptionRepository repository;

    @Autowired
    public MaterialConsumptionService(MaterialConsumptionRepository repository) {
        this.repository = repository;
    }

    public List<MaterialConsumption> getConsumptions(ProductionList productionList) {
        return repository.findByProductionListId(productionList.getId());
    }

    public List<MaterialConsumption> getConsumptionByDate(String materialId, String startDateValue, String endDateValue) {
        Instant startInstant = Instant.parse(startDateValue);
        Instant endInstant = Instant.parse(endDateValue);
        ZonedDateTime startZonedDateTime = startInstant.atZone(ZoneId.of(timeZone));
        ZonedDateTime endZonedDateTime = endInstant.atZone(ZoneId.of(timeZone));

        LocalDateTime startDate = startZonedDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endDate = startDateValue.equals(endDateValue) ?
                endZonedDateTime.toLocalDate().atStartOfDay(): endZonedDateTime.toLocalDate().minusDays(1).atStartOfDay();
        Long id = ((Integer) Integer.parseInt(materialId)).longValue();

        return repository.findByMaterialIdAndProductionListProductionDateBetween(id, startDate, endDate);
    }

    public List<MaterialConsumption> getConsumptionByProductions(List<ProductionList> productionLists) {
        return repository.findByProductionListIn(productionLists);
    }
}
