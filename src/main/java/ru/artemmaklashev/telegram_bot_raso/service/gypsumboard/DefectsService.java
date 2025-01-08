package ru.artemmaklashev.telegram_bot_raso.service.gypsumboard;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.defects.DefectReason;
import ru.artemmaklashev.telegram_bot_raso.entity.defects.DefectTypes;
import ru.artemmaklashev.telegram_bot_raso.entity.defects.Defects;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.BoardDefectsLog;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;
import ru.artemmaklashev.telegram_bot_raso.repositories.ProductionListRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.defects.DefectReasonRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.defects.DefectTypesRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.defects.DefectsLogRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.defects.DefectsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DefectsService {
    private final DefectsLogRepository defectsLogRepository;
    private final DefectReasonRepository defectReasonRepository;
    private final DefectTypesRepository defectTypesRepository;
    private final DefectsRepository repository;
    private final ProductionListRepository productionListRepository;

    @Autowired
    public DefectsService(DefectsLogRepository defectsLogRepository, DefectReasonRepository defectReasonRepository, DefectTypesRepository defectTypesRepository, DefectsRepository repository, ProductionListRepository productionListRepository) {
        this.defectsLogRepository = defectsLogRepository;
        this.defectReasonRepository = defectReasonRepository;
        this.defectTypesRepository = defectTypesRepository;
        this.repository = repository;
        this.productionListRepository = productionListRepository;
    }

    public List<BoardDefectsLog> getDefectsByDate(String startDateString, String endDateString) {
//        LocalDateTime startDate = Utils.convertStringToStartOfTheDay(startDateString);
//        LocalDateTime endDate = Utils.convertStringToStartOfTheDay(endDateString);
        LocalDateTime startDate = LocalDate.parse(startDateString).atStartOfDay();
        LocalDateTime endDate = startDateString.equals(endDateString) ?
                LocalDate.parse(endDateString).atStartOfDay() : LocalDate.parse(endDateString).atStartOfDay().minusDays(1);
        List<ProductionList> productionLogIds = productionListRepository.findProductionListByProductionDateBetween(startDate, endDate);
        return defectsLogRepository.findBoardDefectsLogsByProductionListIn(productionLogIds);
    }

    public List<DefectReason> getDefectReasons() {
        return defectReasonRepository.findAll();
    }

    public List<DefectTypes> getDefectTypes() {
        return defectTypesRepository.findAll();
    }


    public List<Defects> getDefects(int defectReasonId, int defectTypeId) {
        return repository.findAllByDefectReasonIdAndDefectTypesId(defectReasonId, defectTypeId);
    }
}
