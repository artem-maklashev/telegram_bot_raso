package ru.artemmaklashev.telegram_bot_raso.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoardCategory;
import ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard.GypsumBoardCategoriesRepository;

import java.util.List;

@Service
public class GypsumBoardCategoriesService {
    private final GypsumBoardCategoriesRepository categoriesRepository;
    @Autowired
    public GypsumBoardCategoriesService(GypsumBoardCategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<GypsumBoardCategory> getGypsumBoardCategories() {
        return categoriesRepository.findAll();
    }
}
