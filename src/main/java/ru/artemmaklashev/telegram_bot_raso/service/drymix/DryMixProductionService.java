package ru.artemmaklashev.telegram_bot_raso.service.drymix;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixCategoryProduction;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixCategoryProductionRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class DryMixProductionService {
    private final MixCategoryProductionRepository mixCategoryProductionRepository;

    @Value("${spring.jackson.time-zone}")
    private String timeZone;
    @Autowired
    public DryMixProductionService(MixCategoryProductionRepository mixCategoryProductionRepository) {
        this.mixCategoryProductionRepository = mixCategoryProductionRepository;
    }

    public List<MixCategoryProduction> getProductionsByPeriod(String startDate, String endDate) {
        Instant startDateInstant = Instant.parse(startDate);
        Instant endDateInstant = Instant.parse(endDate);
        ZonedDateTime startZonedDateTime = startDateInstant.atZone(ZoneId.of(timeZone));
        ZonedDateTime endZonedDateTime = endDateInstant.atZone(ZoneId.of(timeZone));
        LocalDateTime startDateLocal = startZonedDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endDateLocal = endZonedDateTime.toLocalDate().atStartOfDay();
//        return mixCategoryProductionRepository.findAllByProductionProductionDateBetween(startDateLocal, endDateLocal);
        return mixCategoryProductionRepository.findByProductionProductionDateBetween(startDateLocal, endDateLocal);

    }



}
