package ru.artemmaklashev.telegram_bot_raso.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.specification.Material;
import ru.artemmaklashev.telegram_bot_raso.repositories.specification.MaterialRepository;

import java.util.List;

@Service
public class MaterialService {
    private final MaterialRepository repository;

    @Autowired
    public MaterialService(MaterialRepository repository) {
        this.repository = repository;
    }

    public List<Material> getAll() {
        return repository.findAll();
    }
}
