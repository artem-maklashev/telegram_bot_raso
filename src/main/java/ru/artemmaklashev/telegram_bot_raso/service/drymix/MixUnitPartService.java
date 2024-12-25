package ru.artemmaklashev.telegram_bot_raso.service.drymix;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays.MixUnitPart;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixUnitPartRepository;

import java.util.List;

@Service
public class MixUnitPartService {
    private final MixUnitPartRepository repository;

    @Autowired
    public MixUnitPartService(MixUnitPartRepository repository) {
        this.repository = repository;
    }

    public List<MixUnitPart> getUnitPartsByUnitAndDelayType(Long mixUnitId, Integer delayTypeId) {
        return repository.findAllByUnitIdAndDelayTypeId(mixUnitId, delayTypeId);
    }

}
