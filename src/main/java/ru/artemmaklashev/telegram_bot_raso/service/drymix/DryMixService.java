package ru.artemmaklashev.telegram_bot_raso.service.drymix;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.DryMix;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.DryMixRepository;

import java.util.List;

@Service
public class DryMixService {
    private final DryMixRepository repository;

    @Autowired
    public DryMixService(DryMixRepository repository) {
        this.repository = repository;
    }

    public List<DryMix> getAll() {
        return repository.findAll();
    }
}
