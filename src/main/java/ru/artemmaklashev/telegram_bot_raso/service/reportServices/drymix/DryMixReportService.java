package ru.artemmaklashev.telegram_bot_raso.service.reportServices.drymix;

import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays.MixDelay;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixDelayRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixProductionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DryMixReportService {
    private final MixProductionRepository repository;
    private final MixDelayRepository delayRepository;

    public DryMixReportService(MixProductionRepository repository, MixDelayRepository delayRepository) {
        this.repository = repository;
        this.delayRepository = delayRepository;
    }

    public List<MixDelay> getDelays() {
        LocalDateTime date = LocalDate.now().minusDays(1L).atStartOfDay();
        return delayRepository.findAllByMixProductionProductionDate(date);
    }
}
