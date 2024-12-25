package ru.artemmaklashev.telegram_bot_raso.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.Types;
import ru.artemmaklashev.telegram_bot_raso.repositories.TypesRepository;

@Service
public class TypesService {
    private final TypesRepository typesRepository;

    @Autowired

    public TypesService(TypesRepository typesRepository) {
        this.typesRepository = typesRepository;
    }


    public Types getBoardType() {
        return typesRepository.findById(1).orElse(null);
    }
}
