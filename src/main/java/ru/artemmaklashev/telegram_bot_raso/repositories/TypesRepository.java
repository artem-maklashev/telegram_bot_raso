package ru.artemmaklashev.telegram_bot_raso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.Types;

public interface TypesRepository extends JpaRepository <Types, Integer>{

}
