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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<BoardDelays> result = delaysRepository.findAllByDelayDateBetween(date, date);
        return result;
    }

    public List<GypsumBoardProductionData> getProductionsByInterval() {
        LocalDateTime startDate = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = startDate.plusMonths(1).minusNanos(1); // Весь текущий месяц

        List<BoardProduction> productions = getFilteredProductions(startDate, endDate);
        List<Plan> plans = planRepository.findAllByPlanDateBetween(startDate.toLocalDate(), endDate.toLocalDate());

        Map<Integer, GypsumBoardProductionData> result = processProductions(productions);
        processPlans(plans, result);

        // Вычисляем defectiveValue для каждого элемента и возвращаем список
        return result.values().stream()
                .peek(item -> {
                    if (item.getTotal() != 0) {
                        item.setDefectiveValue(1 - item.getFactValue() / item.getTotal());
                    } else {
                        item.setDefectiveValue(0); // или другое значение по умолчанию
                    }
                })
                .collect(Collectors.toList());
    }

    // Получение отфильтрованного списка производств
    private List<BoardProduction> getFilteredProductions(LocalDateTime startDate, LocalDateTime endDate) {
        return productionRepository.findByProductionListProductionDateBetween(startDate, endDate)
                .stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId() > 1 && p.getCategory().getId() < 5)
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
                    k -> new GypsumBoardProductionData(title, 0, total, fact, 0)
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
                    k -> new GypsumBoardProductionData(title, plan.getPlanValue(), 0, 0, 0)
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
