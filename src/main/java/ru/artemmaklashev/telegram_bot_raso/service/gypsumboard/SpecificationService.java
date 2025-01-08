package ru.artemmaklashev.telegram_bot_raso.service.gypsumboard;


import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.specification.MaterialConsumption;
import ru.artemmaklashev.telegram_bot_raso.entity.specification.Specification;
import ru.artemmaklashev.telegram_bot_raso.repositories.specification.MaterialConsumptionRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.specification.SpecificationRepository;

import java.util.List;

@Service
public class SpecificationService {
    private static final Logger logger = LoggerFactory.getLogger(MaterialConsumptionService.class);

    private final SpecificationRepository repository;
    private final MaterialConsumptionRepository consumptionRepository;

    @Autowired
    public SpecificationService(SpecificationRepository repository, MaterialConsumptionRepository consumptionRepository) {
        this.repository = repository;
        this.consumptionRepository = consumptionRepository;
    }

    public List<Specification> getSpecificationByProduct(GypsumBoard gypsumBoard) {
        return repository.getSpecificationsByProductId(gypsumBoard.getId());
    }

    public MaterialConsumption updateConsumption(MaterialConsumption materialConsumption) {
        MaterialConsumption oldConsumption = consumptionRepository.findFirstById(materialConsumption.getId());

        if (oldConsumption != null) {
            oldConsumption.setProductionList(materialConsumption.getProductionList());
            oldConsumption.setMaterial(materialConsumption.getMaterial());
            oldConsumption.setQuantity(materialConsumption.getQuantity());
            consumptionRepository.save(oldConsumption);
            logger.info("Updated MaterialConsumption with ID: {}", oldConsumption.getId());
            return oldConsumption;
        } else {
            String errorMsg = "Consumption not found with ProductionList ID: " + materialConsumption.getProductionList().getId();
            logger.error(errorMsg);
            throw new EntityNotFoundException(errorMsg);
        }
    }

    public MaterialConsumption addConsumption(MaterialConsumption materialConsumption) {
        materialConsumption.setId(0L);
        return consumptionRepository.save(materialConsumption);
    }

    public List<Specification> getAllSpecifications() {
        return repository.findAll();
    }
}
