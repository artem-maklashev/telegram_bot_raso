package ru.artemmaklashev.telegram_bot_raso.service.drymix;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays.MixProductionArea;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixProductionAreaRepository;

import java.util.List;

@Service
public class MixProductionAreaService {
    private final MixProductionAreaRepository repository;

    @Autowired
    public MixProductionAreaService(MixProductionAreaRepository repository) {
        this.repository = repository;
    }

    public List<MixProductionArea> getAll() {
        return repository.findAll();
    }
}
