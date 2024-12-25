package ru.artemmaklashev.telegram_bot_raso.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.Plan;
import ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard.PlanRepository;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class PlanService {
    static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private final PlanRepository planRepository;
    @Value("${spring.jackson.time-zone}")
    private String timeZone;

    @Autowired
    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public List<Plan> getPlanData() {
        LocalDate date = LocalDate.now();
        LocalDate startDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        YearMonth yearMonth = YearMonth.from(date);
        LocalDate endDate = yearMonth.atEndOfMonth();
        System.out.println(this.planRepository.findPlansByPlanDateBetween(startDate, endDate).isEmpty());
        return this.planRepository.findPlansByPlanDateBetween(startDate, endDate);
    }

    public List<Plan> getPlans(String startDateString, String endDateString)  {
        Instant startInstant = Instant.parse(startDateString);
        Instant endInstant = Instant.parse(endDateString);
        ZonedDateTime startZonedDateTime = startInstant.atZone(ZoneId.of(timeZone));
        ZonedDateTime endZonedDateTime = endInstant.atZone(ZoneId.of(timeZone));

        LocalDate startDate = startZonedDateTime.toLocalDate();
        LocalDate endDate = startDateString.equals(endDateString) ?
                endZonedDateTime.toLocalDate() : endZonedDateTime.toLocalDate().minusDays(1);
        System.out.println("Запрос плана за период получен");
        System.out.println("Период: " + startDate + " - " + endDate);
        return this.planRepository.findPlansByPlanDateBetween(startDate, endDate);
    }

    public List<Plan> getPlanByMonth(String dateString) {
        Instant startInstant = Instant.parse(dateString);
        ZonedDateTime startZonedDateTime = startInstant.atZone(ZoneId.of(timeZone));
        ZonedDateTime endZonedDateTime = startInstant.atZone(ZoneId.of(timeZone)).with(TemporalAdjusters.lastDayOfMonth());
        LocalDate startDate = startZonedDateTime.toLocalDate();
        LocalDate endDate = endZonedDateTime.toLocalDate();
        return this.planRepository.findPlansByPlanDateBetween(startDate, endDate);
    }
    @Transactional
    public Plan savePlanData(Plan plan) {
            return this.planRepository.save(plan);
    }

    @Transactional
    public void deletePlanData(Integer id) {
        this.planRepository.deleteById(id);
    }



}
