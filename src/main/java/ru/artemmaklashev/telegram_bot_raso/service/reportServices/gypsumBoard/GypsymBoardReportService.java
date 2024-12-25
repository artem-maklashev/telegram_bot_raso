package ru.artemmaklashev.telegram_bot_raso.service.reportServices.gypsumBoard;

import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.BoardDelays;
import ru.artemmaklashev.telegram_bot_raso.entity.production.BoardProduction;
import ru.artemmaklashev.telegram_bot_raso.repositories.delays.BoardsDelaysRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard.BoardProductionRepository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GypsymBoardReportService {
    private final BoardProductionRepository productionRepository;
    private final BoardsDelaysRepository delaysRepository;


    public GypsymBoardReportService(BoardProductionRepository productionRepository, BoardsDelaysRepository delaysRepository) {
        this.productionRepository = productionRepository;
        this.delaysRepository = delaysRepository;
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
}
