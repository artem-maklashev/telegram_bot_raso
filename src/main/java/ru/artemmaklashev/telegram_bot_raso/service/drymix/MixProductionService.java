package ru.artemmaklashev.telegram_bot_raso.service.drymix;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixProduction;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixProductionRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class MixProductionService {
    @Value("${spring.jackson.time-zone}")
    private String timeZone;

    private final MixProductionRepository repository;

    public MixProductionService(MixProductionRepository repository) {
        this.repository = repository;
    }

    public List<MixProduction> findLast10productions() {
        return repository.findTop10ByOrderByProductionDateDesc();
    }


    public MixProduction updateProduction(MixProduction production) {
        MixProduction existingProduction = repository.findById(production.getId()).orElse(null);
        if (existingProduction != null) {
            existingProduction.setProductionStart(production.getProductionStart());
            existingProduction.setProductionDate(production.getProductionDate());
            existingProduction.updateProductionDate();
            existingProduction.setMix(production.getMix());
            existingProduction.setShift(production.getShift());
            System.out.println("Изменяю существующий выпоуск смесей");
            return repository.save(existingProduction);
        } else {
            production.updateProductionDate();
            System.out.println("Сохраняю новый выпуск смесей");
            return repository.save(production);
        }
    }

    public MixProduction deleteProduction(Long id) {
        MixProduction existingProduction = repository.findById(id).orElse(null);
        if (existingProduction != null) {
            repository.delete(existingProduction);
            return existingProduction;
        } else {
            return null;
        }
    }

    public List<MixProduction> findProductions(String startDate, String endDate) {
        Instant startDateInstant = Instant.parse(startDate);
        Instant endDateInstant = Instant.parse(endDate);
        ZonedDateTime startZonedDateTime = startDateInstant.atZone(ZoneId.of(timeZone));
        ZonedDateTime endZonedDateTime = endDateInstant.atZone(ZoneId.of(timeZone));
        LocalDateTime start = startZonedDateTime.toLocalDate().atStartOfDay();
        LocalDateTime end = endZonedDateTime.toLocalDate().atStartOfDay();
        return repository.findAllByProductionDateBetween(start, end);
    }

    public List<MixProduction> getLast10Productions() {
        return repository.findTop10ByOrderByProductionDateDesc();
    }
}

