package ru.artemmaklashev.telegram_bot_raso.controller.drymix;

import org.springframework.stereotype.Component;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.BoardDelays;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays.MixDelay;
import ru.artemmaklashev.telegram_bot_raso.service.reportServices.drymix.DryMixReportService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DryMixController {
    private  final DryMixReportService dryMixReportService;

    public DryMixController(DryMixReportService dryMixReportService) {
        this.dryMixReportService = dryMixReportService;
    }

    public String getDelaysData() {
        List<MixDelay> delays = dryMixReportService.getDelays();
        return formatDelays(delays);
    }

    private String formatDelays(List<MixDelay> delays) {
        if (delays.isEmpty()) {
            return "Простоев нет!";
        }

        Map<String, Long> result = delays.stream()
                .collect(Collectors.toMap(
                        delay -> delay.getMixUnitPart().getUnit().getName() + "->" + delay.getMixUnitPart().getName(),
                        MixDelay:: getDuration,  // Преобразование в Float
                        Long::sum
                ));
        // Формируем строковое представление для вывода.
        StringBuilder sb = new StringBuilder();
        sb.append("Простои за указанный период составляют:").append(delays.stream().mapToLong(MixDelay::getDuration).sum()).append(" мин\n")
                .append("В том числе:").append("\n");
        result.forEach((key, value) -> sb.append(key).append(": ").append(value).append("мин\n"));

        return sb.toString();
    }
}
