package ru.artemmaklashev.telegram_bot_raso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemmaklashev.telegram_bot_raso.entity.Shift;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.*;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;
import ru.artemmaklashev.telegram_bot_raso.repositories.ProductionListRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.delays.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardDelaysService {
    private final BoardsDelaysRepository boardsDelaysRepository;
    private final DivisionRepository divisionRepository;
    private final ProductionAreaRepository productionAreaRepository;
    private final UnitRepository unitRepository;
    private final UnitPartRepository unitPartRepository;

    @Autowired
    public BoardDelaysService(
            BoardsDelaysRepository boardsDelaysRepository, DivisionRepository divisionRepository, ProductionListRepository productionListRepository, ProductionAreaRepository productionAreaRepository, UnitRepository unitRepository, UnitPartRepository unitPartRepository) {
        this.boardsDelaysRepository = boardsDelaysRepository;
        this.divisionRepository = divisionRepository;
        this.productionAreaRepository = productionAreaRepository;
        this.unitRepository = unitRepository;
        this.unitPartRepository = unitPartRepository;
    }

    public List<BoardDelays> getDelaysByDate(String startDate, String endDate) {
//        LocalDate start = Utils.convertStringToDate(startDate).toLocalDate();
//        LocalDate end = Utils.convertStringToDate(endDate).toLocalDate();
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = startDate.equals(endDate) ?  LocalDate.parse(endDate) : LocalDate.parse(endDate).minusDays(1);
        System.out.println("Данные по простоям в размере " + boardsDelaysRepository.findAllByDelayDateBetween(start, end).size() + " получены");
//        System.out.println(boardsDelaysRepository.findIdsInDateRange(start, end).size());
        return boardsDelaysRepository.findAllByDelayDateBetween(start, end);
    }

    public List<BoardDelays> getDaleysByProductionList(ProductionList p, GypsumBoard gb) {
        return boardsDelaysRepository.findAllByDelayDateAndShiftAndProduct(p.getProductionDate().toLocalDate(), p.getShift(), gb);
    }

    @Transactional
    public List<BoardDelays> updateBoardDelays(List<BoardDelays> delays) {
        System.out.println("Обновление простоев");
        List<BoardDelays> updatedBoardDelays = new ArrayList<>();

        if (delays == null || delays.isEmpty()) {
            return updatedBoardDelays;
        }

        // Устанавливаем правильную дату во всех простоях
        delays.forEach(BoardDelays::setDelayDate);

        // Найти существующие задержки по дате, смене и продукту
        List<BoardDelays> oldDelays = boardsDelaysRepository.findByDelayDateAndShiftAndProduct(
                delays.get(0).getDelayDate(),
                delays.get(0).getShift(),
                delays.get(0).getProduct()
        );

        // Обновить существующие и добавить новые задержки
        for (BoardDelays delay : delays) {
            Optional<BoardDelays> oldDelayOpt = oldDelays.stream()
                    .filter(d -> d.equals(delay))
                    .findFirst();

            if (oldDelayOpt.isPresent()) {
                BoardDelays oldDelay = oldDelayOpt.get();
                System.out.println("Обновляем простои:");
                oldDelay.setDelayType(delay.getDelayType());
                oldDelay.setShift(delay.getShift());
                oldDelay.setProduct(delay.getProduct());
                oldDelay.setStartTime(delay.getStartTime());
                oldDelay.setDelayDate();///
                oldDelay.setEndTime(delay.getEndTime());
                oldDelay.setUnitPart(delay.getUnitPart());
                updatedBoardDelays.add(oldDelay);
                boardsDelaysRepository.save(oldDelay);
            } else {
                System.out.println("Добавляем простои");
                delay.setDelayDate();
                BoardDelays result = boardsDelaysRepository.save(delay);
                System.out.println("Результат добавления простоя: " + result);
                updatedBoardDelays.add(result);
            }
        }

        // Найти и удалить задержки, которые не присутствуют в новом списке
        List<BoardDelays> differences = oldDelays.stream()
                .filter(x -> !delays.contains(x))
                .toList();

        if (!differences.isEmpty()) {
            System.out.println("Удаляем простои");
            for (BoardDelays delay : differences) {
                System.out.println("Удаляем простои: " + delay.getDelayDate() + " " + delay.getShift() + " " + delay.getProduct());
                boardsDelaysRepository.delete(delay);
            }
        }

        return updatedBoardDelays;
    }

    public List<Division> getDivisions() {
        return divisionRepository.findAll();
    }

    public List<ProductionArea> getProductionAreas(int divisionId) {
        return productionAreaRepository.findAllByDivisionId(divisionId);
    }

    public List<Unit> getUnitsByAreaId(int id) {
        return unitRepository.findAllByProductionAreaId(id);
    }

    public List<UnitPart> getUnitPartByUnitId(int id) {
        return unitPartRepository.findAllByUnitId(id);
    }

    public List<BoardDelays> saveBoardDelays(List<BoardDelays> delays) {
        for (BoardDelays delay : delays) {
//            if (delay.getStartTime().getHour() < 8  ){
//                delay.setDelayDate(delay.getStartTime().toLocalDate().minusDays(1L));
//            } else {
//                delay.setDelayDate(delay.getStartTime().toLocalDate());
//            }

            delay.setDelayDate();
            System.out.println("Дата окончания простоя" + delay.getEndTime() + " -> " + delay.getEndTime());
            delay.setEndTime(delay.getEndTime());
            System.out.println("Дата начала простоя" + delay.getStartTime() + " -> " + delay.getDelayDate());
            delay.setStartTime(delay.getStartTime());
        }
        return boardsDelaysRepository.saveAll(delays);
    }

    public void deleteBoardDelays(LocalDate productionDate, Shift shift, GypsumBoard product) {
        boardsDelaysRepository.deleteAllByDelayDateAndShiftAndProduct(productionDate, shift, product);
    }
}
