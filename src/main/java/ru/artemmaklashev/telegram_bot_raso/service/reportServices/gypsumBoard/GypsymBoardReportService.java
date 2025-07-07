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
        LocalDateTime startDate = LocalDate.now().atStartOfDay();
        LocalDateTime endDate = startDate.withDayOfMonth(1);

        List<BoardProduction> data = productionRepository.findByProductionListProductionDateBetween(startDate, endDate).
                stream().filter(p -> p.getCategory().getId() > 1 && p.getCategory().getId() < 5).toList();
        List<Plan> planData = planRepository.findAllByPlanDateBetween(startDate, endDate);

        Map<Integer, GypsumBoardProductionData> result = new HashMap<>();

        for (BoardProduction production : data) {
            int key = production.getProduct().getId();
            float plan = 0;
            float total = 0;
            float fact = 0;
            float defect = 0;
            String title = getGypsumBoardName(production.getProduct());
            if (production.getCategory().getId() == 1) {
                total = production.getValue();
            } else fact = production.getValue();

            if (result.containsKey(key)) {
                result.get(key).addValues(plan, total, fact, defect);
            } else {
                GypsumBoardProductionData data1 = new GypsumBoardProductionData(title, plan, total, fact, defect);
            }
        }


        for (Plan plan : planData) {
            if (result.containsKey(plan.getGypsumBoard().getId())) {
                result.get(plan.getGypsumBoard().getId()).addValues(plan.getPlanValue(), 0, 0, 0);
            } else {
                GypsumBoardProductionData data1 = new GypsumBoardProductionData(getGypsumBoardName(plan.getGypsumBoard()), plan.getPlanValue(), 0, 0, 0);
                result.put(plan.getGypsumBoard().getId(), data1);
            }
        }
        List<GypsumBoardProductionData> resultData = new ArrayList<>();
        for (Map.Entry<Integer, GypsumBoardProductionData> entry : result.entrySet()) {
            resultData.add(entry.getValue());
        }

        return resultData;


    }

    private String getGypsumBoardName(GypsumBoard gb) {
        return gb.getTradeMark().getName() + " " + gb.getBoardType().getName() +
                "-" + gb.getEdge().getName() + " " + gb.getThickness().getValue() +
                "-" + gb.getWidth().getValue() + "-" + gb.getLength().getValue();
    }
}
