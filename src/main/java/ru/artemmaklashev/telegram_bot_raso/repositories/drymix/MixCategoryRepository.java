package ru.artemmaklashev.telegram_bot_raso.repositories.drymix;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixCategory;

public interface MixCategoryRepository extends JpaRepository<MixCategory, Long>{
}
