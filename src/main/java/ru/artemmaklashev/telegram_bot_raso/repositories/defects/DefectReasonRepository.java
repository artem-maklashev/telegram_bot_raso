package ru.artemmaklashev.telegram_bot_raso.repositories.defects;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.defects.DefectReason;

public interface DefectReasonRepository extends JpaRepository<DefectReason, Integer> {

}
