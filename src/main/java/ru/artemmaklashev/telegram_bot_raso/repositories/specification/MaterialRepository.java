package ru.artemmaklashev.telegram_bot_raso.repositories.specification;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.specification.Material;


public interface MaterialRepository extends JpaRepository<Material, Long>{

}
