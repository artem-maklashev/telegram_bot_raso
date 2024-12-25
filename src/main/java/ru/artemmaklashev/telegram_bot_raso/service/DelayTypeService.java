package ru.artemmaklashev.telegram_bot_raso.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.DelayType;
import ru.artemmaklashev.telegram_bot_raso.repositories.DelayTypeRepository;

import java.util.List;

@Service
public class DelayTypeService  {
    private final DelayTypeRepository delayTypeRepository;

    @Autowired
    public DelayTypeService(DelayTypeRepository delayTypeRepository) {
        this.delayTypeRepository = delayTypeRepository;
    }

    public List<DelayType> getDelayTypes() {
        return delayTypeRepository.findAll();
    }
}
