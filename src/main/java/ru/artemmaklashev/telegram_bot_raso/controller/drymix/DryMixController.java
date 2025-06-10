package ru.artemmaklashev.telegram_bot_raso.controller.drymix;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.BoardDelays;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.DryMix;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays.MixDelay;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixCategoryProduction;
import ru.artemmaklashev.telegram_bot_raso.service.report.ASCIItableImage;
import ru.artemmaklashev.telegram_bot_raso.service.reportServices.drymix.DryMixReportService;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
        return getString(result, delays.stream().mapToLong(MixDelay::getDuration), delays);
    }

    @NotNull
    public static String getString(Map<String, Long> result, LongStream longStream, List<MixDelay> delays) {
        StringBuilder sb = new StringBuilder();
        sb.append("Простои за указанный период составляют:").append(longStream.sum()).append(" мин\n")
                .append("В том числе:").append("\n");
        result.forEach((key, value) -> sb.append(key).append(": ").append(value).append("мин\n"));

        return sb.toString();
    }

//    private String formatMixProductions(List<MixCategoryProduction> productions) {
//
//        if (productions.isEmpty()) {
//            return "Нет выпуска за указанную дату";
//        }
//        Map<String, Integer> result = fetchMixData(productions);
//        String resultTable = new ASCIItable(result, List.of("Продукция", "Кол-во")).drawTable();
//        String totalValue = String.format("%.0f",productions.stream().mapToDouble(MixCategoryProduction::getQuantity).sum());
//        return "**Выпуск продукции за " + LocalDate.now().minusDays(1L).format(DateTimeFormatter.ISO_LOCAL_DATE)+"**\n" +
//                resultTable + "\n" + "**Итого: " + totalValue + " м"+"²**";
//    }

    private Map<String, Integer> fetchMixData(List<MixCategoryProduction> productions) {
        return productions.stream()
                .collect(Collectors.toMap(
                        production -> {
                            DryMix mix = production.getProduction().getMix();
                            return mix.getName();
                        },
                        production -> Math.round(production.getQuantity()),  // Преобразуем float в int, чтобы отбросить дробную часть
                        Integer::sum
                ));
    }

    public BufferedImage getImageReport() {
        List<MixCategoryProduction> productions = dryMixReportService.getLastProductions().stream()
                .filter(mixproduction -> mixproduction.getCategory().getId()  == 2)
                .toList();
        if (productions.isEmpty()) {
            return null;
        }

        // Собираем данные в Map, где ключ — это описание продукции, а значение — её количество

        Map<String, Integer> result = fetchMixData(productions);;
        return new ASCIItableImage(result, List.of("Продукция", "Кол-во")).drawTable();
    }
}
