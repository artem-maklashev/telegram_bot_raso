package ru.artemmaklashev.telegram_bot_raso.service.reportServices.gypsumBoard;

import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.BoardDelays;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.Plan;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.GypsumBoardProductionData;
import ru.artemmaklashev.telegram_bot_raso.entity.production.BoardProduction;
import ru.artemmaklashev.telegram_bot_raso.repositories.delays.BoardsDelaysRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard.BoardProductionRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard.PlanRepository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GypsymBoardReportService {
    private final BoardProductionRepository productionRepository;
    private final BoardsDelaysRepository delaysRepository;

    private final PlanRepository planRepository;


    public GypsymBoardReportService(BoardProductionRepository productionRepository, BoardsDelaysRepository delaysRepository, PlanRepository planRepository) {
        this.productionRepository = productionRepository;
        this.delaysRepository = delaysRepository;
        this.planRepository = planRepository;
    }

    public List<BoardProduction> getLastProductions() {
        LocalDateTime date = LocalDate.now().minusDays(1L).atStartOfDay();
        System.out.println("Запрашиваю данные за : " + date);
        List<BoardProduction> result = productionRepository.findByProductionListProductionDateBetween(date, date);
        System.out.println("Получены данные в размере: " + result.size());
        return result;
    }

    public List<BoardDelays> getLastDelays() {
        LocalDate date = LocalDate.now().minusDays(1L);
        return delaysRepository.findAllByDelayDateBetween(date, date);
    }

    public List<GypsumBoardProductionData> getProductionsByInterval() {
        // 1. Определение временного интервала
        LocalDate now = LocalDate.now();
        LocalDateTime startDate;
        LocalDateTime endDate = now.minusDays(1).atStartOfDay(); // Конец периода - вчера

        if (now.getDayOfMonth() != 1) {
            startDate = now.withDayOfMonth(1).atStartOfDay(); // Начало текущего месяца
        } else {
            startDate = now.minusMonths(1).withDayOfMonth(1).atStartOfDay(); // Начало предыдущего месяца
        }

        // 2. Получение данных
        List<BoardProduction> productions = getFilteredProductions(startDate, endDate);
        List<Plan> plans = planRepository.findAllByPlanDateBetween(
                startDate.toLocalDate(),
                endDate.toLocalDate()
        );

        // 3. Обработка данных
        Map<Integer, GypsumBoardProductionData> result = processProductions(productions);
        processPlans(plans, result);

        // 4. Расчет и возврат результата
        return result.values().stream()
                .peek(this::calculateDefectiveValue)
                .sorted(Comparator.comparingDouble(GypsumBoardProductionData::getPlanValue).reversed())
                .collect(Collectors.toList());
    }

    private void calculateDefectiveValue(GypsumBoardProductionData item) {
        double defectiveValue = (item.getTotal() != 0)
                ? (1 - item.getFactValue() / item.getTotal())
                : 0;
        item.setDefectiveValue((float) defectiveValue);
    }

    // Получение отфильтрованного списка производств
    private List<BoardProduction> getFilteredProductions(LocalDateTime startDate, LocalDateTime endDate) {
        return productionRepository.findByProductionListProductionDateBetween(startDate, endDate)
                .stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId() >= 1 && p.getCategory().getId() < 5)
                .toList();
    }

    // Обработка данных производства
    private Map<Integer, GypsumBoardProductionData> processProductions(List<BoardProduction> productions) {
        Map<Integer, GypsumBoardProductionData> result = new HashMap<>();

        for (BoardProduction production : productions) {
            if (production.getProduct() == null) continue;

            int productId = production.getProduct().getId();
            String title = getGypsumBoardName(production.getProduct());

            float total = production.getCategory().getId() == 1 ? production.getValue() : 0;
            float fact = production.getCategory().getId() != 1 ? production.getValue() : 0;

            GypsumBoardProductionData data = result.computeIfAbsent(
                    productId,
                    k -> new GypsumBoardProductionData(title, 0, 0, 0, 0)
            );

            data.addValues(0, total, fact, 0); // Обновляем значения
        }


        return result;
    }

    // Обработка данных плана
    private void processPlans(List<Plan> plans, Map<Integer, GypsumBoardProductionData> result) {
        for (Plan plan : plans) {
            if (plan.getGypsumBoard() == null) continue;

            int boardId = plan.getGypsumBoard().getId();
            String title = getGypsumBoardName(plan.getGypsumBoard());

            GypsumBoardProductionData data = result.computeIfAbsent(
                    boardId,
                    k -> new GypsumBoardProductionData(title, 0, 0, 0, 0)
            );

            data.addValues(plan.getPlanValue(), 0, 0, 0);
        }
    }

    private String getGypsumBoardName(GypsumBoard gb) {
        return gb.getTradeMark().getName() + " " + gb.getBoardType().getName() +
                "-" + gb.getEdge().getName() + " " + gb.getThickness().getValue() +
                "-" + gb.getWidth().getValue() + "-" + gb.getLength().getValue();
    }
}
