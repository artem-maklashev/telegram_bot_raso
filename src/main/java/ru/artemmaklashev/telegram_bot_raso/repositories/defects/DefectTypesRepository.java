package ru.artemmaklashev.telegram_bot_raso.repositories.defects;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.defects.DefectTypes;

public interface DefectTypesRepository extends JpaRepository<DefectTypes, Integer>{
}
