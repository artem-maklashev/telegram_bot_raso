package ru.artemmaklashev.telegram_bot_raso.service.reportServices.drymix;

import io.micrometer.common.KeyValues;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.BoardDelays;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays.MixDelay;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixCategoryProduction;
import ru.artemmaklashev.telegram_bot_raso.entity.production.BoardProduction;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixCategoryProductionRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixDelayRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixProductionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DryMixReportService {
    private final MixCategoryProductionRepository repository;
    private final MixDelayRepository delayRepository;

    public DryMixReportService(MixCategoryProductionRepository repository, MixDelayRepository delayRepository) {
        this.repository = repository;
        this.delayRepository = delayRepository;
    }

    public List<MixDelay> getDelays() {
        LocalDateTime date = LocalDate.now().minusDays(1L).atStartOfDay();
        return delayRepository.findAllByMixProductionProductionDate(date);
    }

    public List<MixCategoryProduction> getLastProductions() {
        LocalDateTime date = LocalDate.now().minusDays(1L).atStartOfDay();
        System.out.println("Запрашиваю данные за : " + date);
        List<MixCategoryProduction> result = repository.findByProductionProductionDateBetween(date, date);
        System.out.println("Получены данные в размере: " + result.size());
        return result;
    }
}
