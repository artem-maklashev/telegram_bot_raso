package ru.artemmaklashev.telegram_bot_raso.service.gypsumboard;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;
import ru.artemmaklashev.telegram_bot_raso.repositories.ProductionListRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class ProductionListService {
    @Value("${spring.jackson.time-zone}")
    private String timeZone;
    private final ProductionListRepository productionListRepository;

    @Autowired
    public ProductionListService(ProductionListRepository productionListRepository) {
        this.productionListRepository = productionListRepository;
    }

    public List<ProductionList> getLast10productions() {
        return productionListRepository.findTop10ByOrderByProductionDateDesc();
    }
    @Transactional
    public ProductionList updateProductionList(ProductionList productionList) {
        ProductionList pl = productionListRepository.findById(productionList.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Production list not found"));

        // Проверка на null уже не нужна, так как мы получили объект из базы данных
        System.out.println("Получены данные для изменения ProductionList");
        System.out.println(pl.getId() + " " + pl.getProductionStart() + " -> " + productionList.getProductionStart());
        pl.setProductionStart( productionList.getProductionStart());
        pl.setProductionFinish(productionList.getProductionFinish());
        pl.updateProductionDate();
        pl.setShift(productionList.getShift());

        // Сохраняем изменения в базу данных
        productionListRepository.save(pl);

        return pl;
    }
    @Transactional
    public ProductionList saveProductionList(ProductionList productionList) {
        productionList.updateProductionDate();
        //
        if (productionList.getId() == -1) {
            productionList.setId(0);
        }
        productionListRepository.save(productionList);
        return productionList;
    }

    public ProductionList getProductionListById(Long id) {
        return productionListRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Production list not found"));
    }

    public void deleteProductionList(Long id) {
        productionListRepository.deleteById(id);
    }

    public List<ProductionList> getBoardProductionsByDate(String dateString) {
        Instant dateInstant = Instant.parse(dateString);
        ZonedDateTime startZonedDateTime = dateInstant.atZone(ZoneId.of(timeZone));
        LocalDateTime date = startZonedDateTime.toLocalDate().atStartOfDay();
        return productionListRepository.findAllByProductionDate(date);
    }
}

