package ru.artemmaklashev.telegram_bot_raso.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.Thickness;
import ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard.ThicknessRepository;

import java.util.List;

@Service
public class ThicknessService {
    private final ThicknessRepository thicknessRepository;

    @Autowired
    public ThicknessService(ThicknessRepository thicknessRepository) {
        this.thicknessRepository = thicknessRepository;
    }

    public List<Thickness> getAll() {
        return thicknessRepository.findAll();
    }
}
