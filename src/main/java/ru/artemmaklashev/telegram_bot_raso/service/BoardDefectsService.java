package ru.artemmaklashev.telegram_bot_raso.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.BoardDefectsLog;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;
import ru.artemmaklashev.telegram_bot_raso.repositories.defects.DefectsLogRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BoardDefectsService {
    private final DefectsLogRepository repository;
    @Autowired
    public BoardDefectsService(DefectsLogRepository repository) {
        this.repository = repository;
    }

    public List<BoardDefectsLog> getBoardDefectsLogByProductionList(long productionId) {
        return repository.findByProductionListId(productionId);
    }

    public void deleteBoardProduction(Long id) {
        repository.deleteAllByProductionListId(id);
    }

    public List<BoardDefectsLog> saveBoardDefects(List<BoardDefectsLog> defectsLogs, ProductionList productionList) {
        defectsLogs.forEach(defectsLog -> defectsLog.setProductionList(productionList));
        return repository.saveAll(defectsLogs);
    }

    public List<BoardDefectsLog> updateBoardDefects(List<BoardDefectsLog> defectsLogs, ProductionList productionList) {
        List<BoardDefectsLog> updatedDefects = new ArrayList<>();

        // Получаем существующие дефекты для данной ProductionList
        List<BoardDefectsLog> oldDefects = repository.findByProductionListId(productionList.getId());

        // Получаем все дефекты из базы данных, чтобы избежать множественных запросов
        Map<Long, BoardDefectsLog> oldDefectsMap = oldDefects.stream()
                .collect(Collectors.toMap(BoardDefectsLog::getId, d -> d));

        for (BoardDefectsLog defectsLog : defectsLogs) {
            BoardDefectsLog oldDefect = oldDefectsMap.remove(defectsLog.getId());

            if (oldDefect != null) {
                // Обновляем существующий дефект
                oldDefect.setProductionList(productionList);
                oldDefect.setDefects(defectsLog.getDefects());
                oldDefect.setValue(defectsLog.getValue());
                updatedDefects.add(oldDefect);
            } else {
                // Добавляем новый дефект
                defectsLog.setProductionList(productionList);
                updatedDefects.add(defectsLog);
            }
        }

        // Удаляем дефекты, которые больше не используются
        if (!oldDefectsMap.isEmpty()) {
            repository.deleteAll(oldDefectsMap.values());
        }

        // Сохраняем обновленные дефекты
        repository.saveAll(updatedDefects);

        return updatedDefects;
    }
}
