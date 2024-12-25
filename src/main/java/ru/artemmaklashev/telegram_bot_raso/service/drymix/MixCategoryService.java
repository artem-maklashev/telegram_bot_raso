package ru.artemmaklashev.telegram_bot_raso.service.drymix;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixCategory;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixCategoryRepository;

import java.util.List;

@Service
public class MixCategoryService {


    private final MixCategoryRepository mixCategoryRepository;
    @Autowired
    public MixCategoryService(MixCategoryRepository mixCategoryRepository) {
        this.mixCategoryRepository = mixCategoryRepository;
    }

    public List<MixCategory> getAll() {
        return mixCategoryRepository.findAll();
    }
}
