package ru.artemmaklashev.telegram_bot_raso.repositories.specification;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.specification.Specification;

import java.util.List;

public interface SpecificationRepository extends JpaRepository<Specification, Long> {
    List<Specification> getSpecificationsByProductId(int id);
}
