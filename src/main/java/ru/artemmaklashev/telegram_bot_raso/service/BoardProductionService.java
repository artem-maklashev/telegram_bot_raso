package ru.artemmaklashev.telegram_bot_raso.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemmaklashev.telegram_bot_raso.entity.Shift;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoardCategory;
import ru.artemmaklashev.telegram_bot_raso.entity.production.BoardProduction;
import ru.artemmaklashev.telegram_bot_raso.entity.production.Production;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;
import ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard.BoardProductionRepository;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
public class BoardProductionService {

    @Value("${spring.jackson.time-zone}")
    private String timeZone;
    private final BoardProductionRepository boardProductionRepository;
    private final ProductionListService productionListService;
    private final BoardDefectsService boardDefectsService;
    private final BoardDelaysService boardDelaysService;

    @Autowired
    public BoardProductionService(BoardProductionRepository boardProductionRepository, ProductionListService productionListService, BoardDefectsService boardDefectsService, BoardDelaysService boardDelaysService) {
        this.boardProductionRepository = boardProductionRepository;
        this.productionListService = productionListService;
        this.boardDefectsService = boardDefectsService;
        this.boardDelaysService = boardDelaysService;
    }


    public List<BoardProduction> getLast10BoardProductions(List<Long> ids) {
        return boardProductionRepository.findDistinctByProductionLogIdIn(ids);
    }


    public List<BoardProduction> getBoardProductionsByProductionListId(long id) {
        return boardProductionRepository.findByProductionListId(id);
    }

    @Transactional
    public List<BoardProduction> updateBoardProduction(
            List<BoardProduction> productions, ProductionList productionList) {

        List<BoardProduction> updatedProductions = new ArrayList<>();

        for (Production<GypsumBoardCategory, GypsumBoard> boardProduction : productions) {
            BoardProduction oldProduction = boardProductionRepository.findBoardProductionById(boardProduction.getId());

            if (oldProduction != null) {
                oldProduction.setProduct(boardProduction.getProduct());
                oldProduction.setProductionList(productionList);
                oldProduction.setCategory(boardProduction.getCategory());
                oldProduction.setValue(boardProduction.getValue());
            }
            updatedProductions.add(oldProduction);
        }

        return boardProductionRepository.saveAll(updatedProductions);
    }

    public List<BoardProduction> saveBoardProduction(List<BoardProduction> productions, ProductionList updatedProductionList) {
        for (BoardProduction production : productions) {
            production.setProductionList(updatedProductionList);
            // Убедитесь, что id установлен в -1 для новых записей
            if (production.getId() == -1) {
                production.setId(0); // Установите id в 0, чтобы JPA сгенерировала новый id
            }
        }
        return boardProductionRepository.saveAll(productions);
    }

    @Transactional
    public void deleteBoardProduction(Long id) {
        ProductionList productionList = productionListService.getProductionListById(id);
        LocalDate productionDate = productionList.getProductionDate().toLocalDate();
        Shift shift = productionList.getShift();
        GypsumBoard product = getBoardProductionsByProductionListId(id).get(0).getProduct();
        System.out.println("Удаляем defects:");
        boardDefectsService.deleteBoardProduction(id);
        System.out.println("Удаляем boardDelays:");
        boardDelaysService.deleteBoardDelays(productionDate, shift, product);
        System.out.println("Удаляем boardProductions:");
        boardProductionRepository.deleteAllByProductionListId(id);
        System.out.println("Удаляем productionList:");
        productionListService.deleteProductionList(id);
    }

    public List<BoardProduction> getProductionsByDate(String date) {
        Instant startInstant = Instant.parse(date);
        ZonedDateTime startZonedDateTime = startInstant.atZone(ZoneId.of(timeZone));
        ZonedDateTime endZonedDateTime = startInstant.atZone(ZoneId.of(timeZone)).with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime startDate = startZonedDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endDate = endZonedDateTime.toLocalDate().atStartOfDay();
        return boardProductionRepository.findByProductionListProductionDateBetween(startDate, endDate);
    }
}
