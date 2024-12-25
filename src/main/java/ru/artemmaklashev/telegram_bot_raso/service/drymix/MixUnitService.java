package ru.artemmaklashev.telegram_bot_raso.service.drymix;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays.MixUnit;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixUnitRepository;

import java.util.List;

@Service
public class MixUnitService{
    private final MixUnitRepository repository;

    @Autowired
    public MixUnitService(MixUnitRepository repository) {
        this.repository = repository;
    }


    public List<MixUnit> getUnitsByProductionArea(Long id) {
        return repository.findAllByProductionAreaId(id);
    }
}
