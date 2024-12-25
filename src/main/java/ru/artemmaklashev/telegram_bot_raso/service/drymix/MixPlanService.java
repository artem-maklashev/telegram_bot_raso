package ru.artemmaklashev.telegram_bot_raso.service.drymix;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixPlan;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixPlanRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MixPlanService {
    @Value("${spring.jackson.time-zone}")
    private String timeZone;
    private final MixPlanRepository mixPlanRepository;

    @Autowired
    public MixPlanService(MixPlanRepository mixPlanRepository) {
        this.mixPlanRepository = mixPlanRepository;
    }


    public List<MixPlan> getPlan(String startDate, String endDate) {
        Instant startDateInstant = Instant.parse(startDate);
        Instant endDateInstant = Instant.parse(endDate);
        ZonedDateTime startZonedDateTime = startDateInstant.atZone(ZoneId.of(timeZone));
        ZonedDateTime endZonedDateTime = endDateInstant.atZone(ZoneId.of(timeZone));
        LocalDate start = startZonedDateTime.toLocalDate();
        LocalDate end = endZonedDateTime.toLocalDate();
        return mixPlanRepository.findByPlanDateBetween(start, end);
    }
    @Transactional
    public MixPlan upsertPlan(MixPlan mixPlan) {
        // Находим план по идентификатору
        Optional<MixPlan> existingPlan = mixPlanRepository.findById(mixPlan.getId());

        if (existingPlan.isPresent()) {
            // Если план найден, обновляем его данные
            MixPlan updatedPlan = existingPlan.get();
            updatedPlan.setDryMix(mixPlan.getDryMix());
            updatedPlan.setPlanDate(mixPlan.getPlanDate());
            updatedPlan.setValue(mixPlan.getValue());

            // Сохраняем изменения и возвращаем обновленный план
            return mixPlanRepository.save(updatedPlan);
        } else {
            // Если план не найден, сохраняем новый план
            return mixPlanRepository.save(mixPlan);
        }
    }

    public Long deletePlan(Long id) {
        mixPlanRepository.deleteById(id);
        return id;
    }



}
