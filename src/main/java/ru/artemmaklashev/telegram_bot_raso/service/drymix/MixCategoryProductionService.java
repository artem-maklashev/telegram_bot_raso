package ru.artemmaklashev.telegram_bot_raso.service.drymix;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixCategoryProduction;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixProduction;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixCategoryProductionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class MixCategoryProductionService {
    private final MixCategoryProductionRepository repository;
    private final MixProductionService mixProductionService;

    @Autowired
    public MixCategoryProductionService(MixCategoryProductionRepository repository, MixProductionService mixProductionService) {
        this.repository = repository;
        this.mixProductionService = mixProductionService;
    }


//    public List<MixCategoryProduction> getLast10Productions() {
//        List<MixProduction> mixProductions = mixProductionService.findLast10productions();
//        List<Long> productionsIds = mixProductions.stream().map(MixProduction::getId).toList();
//        return repository.findAllByProductionIdIn(productionsIds);
//    }

    public List<MixCategoryProduction> saveProductions(List<MixCategoryProduction> productions) {
        List<MixCategoryProduction> result = new ArrayList<>();
        if (!productions.isEmpty()) {
            List<MixCategoryProduction> existingProductions = repository.findAllByProductionId(productions.get(0).getId());
            for (MixCategoryProduction production : productions) {
                MixCategoryProduction existingProduction = repository.findById(production.getId()).orElse(null);
                if (existingProduction == null) {
                    result.add(repository.save(production));
                } else {
                    existingProduction.setCategory(production.getCategory());
                    existingProduction.setProduction(production.getProduction());
                    existingProduction.setQuantity(production.getQuantity());
                    result.add(repository.save(existingProduction));
                }
            }
            for (MixCategoryProduction production : existingProductions) {
                boolean exists = result.stream().filter(p -> Objects.equals(p.getId(), production.getId())).findFirst().orElse(null) != null;
                if (!exists) {
                    repository.delete(production);
                }
            }
        }
        return result;
    }

    public List<MixCategoryProduction> getProductions(String startDate, String endDate) {
        List<MixProduction> mixProductions = mixProductionService.findProductions(startDate, endDate);
        List<Long> productionsIds = mixProductions.stream().map(MixProduction::getId).toList();
        return repository.findAllByProductionIdIn(productionsIds);
    }

    public List<MixCategoryProduction> getCategoriesByProductions(List<MixCategoryProduction> productions) {
        return repository.findAllByProductionIdIn(productions.stream().map(MixCategoryProduction::getId).toList());
    }
}