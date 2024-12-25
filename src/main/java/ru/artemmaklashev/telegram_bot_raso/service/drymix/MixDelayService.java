package ru.artemmaklashev.telegram_bot_raso.service.drymix;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays.MixDelay;
import ru.artemmaklashev.telegram_bot_raso.repositories.drymix.MixDelayRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MixDelayService {
    private final MixDelayRepository mixDelayRepository;

    @Value("${spring.jackson.time-zone}")
    private String timeZone;

    @Autowired
    public MixDelayService(MixDelayRepository mixDelayRepository) {
        this.mixDelayRepository = mixDelayRepository;
    }

    @Transactional
    public List<MixDelay> saveDelays(List<MixDelay> delays, Long productionId) {
        // Получение существующих задержек
        List<MixDelay> existingDelays = mixDelayRepository.findByMixProductionId(productionId);

        if (delays.isEmpty()) {
            // Если новый список задержек пуст, удаляем все существующие
            if (!existingDelays.isEmpty()) {
                mixDelayRepository.deleteAll(existingDelays);
            }
            return List.of(); // Возвращаем пустой список
        }

        // Создание Set для быстрого поиска ID
        Set<Long> delayIds = delays.stream()
                .map(MixDelay::getId)
                .filter(Objects::nonNull) // Фильтрация только ненулевых ID
                .collect(Collectors.toSet());

        // Определение устаревших записей
        List<MixDelay> toDeleteDelays = existingDelays.stream()
                .filter(delay -> !delayIds.contains(delay.getId()))
                .toList();

        // Удаление устаревших записей (если есть)
        if (!toDeleteDelays.isEmpty()) {
            mixDelayRepository.deleteAll(toDeleteDelays);
        }

        // Сохранение новых и обновленных записей
        return mixDelayRepository.saveAll(delays);
    }


    public List<MixDelay> getDelaysByProduction(Long id) {
        return mixDelayRepository.findByMixProductionId(id);
    }

    public MixDelay deleteDelay(Long id) {
        MixDelay mixDelay = mixDelayRepository.findById(id).orElseThrow();
        mixDelayRepository.delete(mixDelay);
        return mixDelay;
    }

    public List<MixDelay> getDelaysByDateBetween(String startDate, String endDate) {
        Instant startDateInstant = Instant.parse(startDate);
        Instant endDateInstant = Instant.parse(endDate);
        ZonedDateTime startZonedDateTime = startDateInstant.atZone(ZoneId.of(timeZone));
        ZonedDateTime endZonedDateTime = endDateInstant.atZone(ZoneId.of(timeZone));
        LocalDateTime startDateLocal = startZonedDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endDateLocal = endZonedDateTime.toLocalDate().atStartOfDay();
        return mixDelayRepository.findAllByMixProductionProductionDateBetween(startDateLocal, endDateLocal);
    }
}
