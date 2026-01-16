package ru.artemmaklashev.telegram_bot_raso.controller.gypsumboard;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.BoardDelays;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.production.BoardProduction;
import ru.artemmaklashev.telegram_bot_raso.service.report.ASCIItable;
import ru.artemmaklashev.telegram_bot_raso.service.report.ASCIItableImage;
import ru.artemmaklashev.telegram_bot_raso.service.reportServices.gypsumBoard.GypsumBoardReportService;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
public class GypsumBoardController {

    private final GypsumBoardReportService gypsumBoardReportService;

    public GypsumBoardController(GypsumBoardReportService gypsumBoardReportService) {
        this.gypsumBoardReportService = gypsumBoardReportService;
    }

    public String getReportData() {

        List<BoardProduction> productions = gypsumBoardReportService.getLastProductions().stream()
                .filter(boardProduction -> boardProduction.getCategory().getId()  > 1 && boardProduction.getCategory().getId() <=4)
                .toList();
        List<BoardDelays> delays = gypsumBoardReportService.getLastDelays();

        return formatBoardDelays(delays);
    }

    private String formatBoardDelays(List<BoardDelays> delays) {
        if (delays.isEmpty()) {
            return "Простоев нет!";
        }

        Map<String, Long> result = delays.stream()
                .collect(Collectors.toMap(
                        delay -> delay.getUnitPart().getUnit().getName() + "->" + delay.getUnitPart().getName(),
                        BoardDelays:: getDuration,  // Преобразование в Float
                        Long::sum
                ));

        // Формируем строковое представление для вывода.
        return getString(result, delays.stream().mapToLong(BoardDelays::getDuration), delays);
    }

    @NotNull
    public static String getString(Map<String, Long> result, LongStream longStream, List<BoardDelays> delays) {
        StringBuilder sb = new StringBuilder();
        sb.append("Простои за указанный период составляют:").append(longStream.sum()).append(" мин\n")
                .append("В том числе:").append("\n");
        result.forEach((key, value) -> sb.append(key).append(": ").append(value).append("мин\n"));

        return sb.toString();
    }


//    private String formatBoardProductions(List<BoardProduction> productions) {
//        if (productions.isEmpty()) {
//            return "Нет выпуска за указанную дату";
//        }
//
//        // Собираем данные в Map, где ключ — это описание продукции, а значение — её количество
//        Map<String, Integer> result = fetchBoardData(productions);;
//        String resultTable = new ASCIItable(result, List.of("Продукция", "Кол-во")).drawTable();
//
//        // Вычисление общей суммы выпуска
//        String totalValue = String.format("%.0f",productions.stream().mapToDouble(BoardProduction::getValue).sum());
//
//        // Формирование итоговой строки с добавлением суммы
////        String resultString = result.entrySet().stream()
////                .map(entry -> entry.getKey() + ": " + entry.getValue())
////                .collect(Collectors.joining("\n", "**Выпуск продукции за " +
////                        LocalDate.now().minusDays(1L).format(DateTimeFormatter.ISO_LOCAL_DATE) + ":**\n", "\n\n**Итого: " + totalValue + " м"+"\u00B2**" ));
//        String resultString = "**Выпуск продукции за " + LocalDate.now().minusDays(1L).format(DateTimeFormatter.ISO_LOCAL_DATE)+"**\n" +
//                resultTable + "\n" + "**Итого: " + totalValue + " м"+"²**";
//        return resultString  ;
//
//    }

    public Map<String, Integer> fetchBoardData(List<BoardProduction> productions) {
        // Преобразуем float в int, чтобы отбросить дробную часть
        return productions.stream()
                .collect(Collectors.toMap(
                        production -> {
                            GypsumBoard gb = production.getProduct();
                            return gb.getTradeMark().getName().substring(0,3) + " " + gb.getBoardType().getName() +
                                    "-" + gb.getEdge().getName() + " " + gb.getThickness().getValue() +
                                    "-" + gb.getWidth().getValue() + "-" + gb.getLength().getValue();
                        },
                        production -> Math.round(production.getValue()),  // Преобразуем float в int, чтобы отбросить дробную часть
                        Integer::sum
                ));
    }

    public BufferedImage getImageReport() {
        List<BoardProduction> allProductions = getLastProductions();
        List<BoardProduction> totalProductions  = allProductions.stream().filter(boardProduction -> boardProduction.getCategory().getId() == 1).toList();
        List<BoardProduction> productions = allProductions.stream()
                .filter(boardProduction -> boardProduction.getCategory().getId()  > 1 && boardProduction.getCategory().getId() <=4)
                .toList();
        if (productions.isEmpty()) {
            return null;
        }
        double totalValue = totalProductions.stream().mapToDouble(BoardProduction::getValue).sum();
        double goodValue = productions.stream().mapToDouble(BoardProduction::getValue).sum();
        Double defectPercent = (totalValue - goodValue) / totalValue * 100;
        // Собираем данные в Map, где ключ — это описание продукции, а значение — её количество

        Map<String, Integer> result = fetchBoardData(productions);;
        return new ASCIItableImage(result, List.of("Продукция", "Кол-во"), defectPercent).drawTable();
    }

    private List<BoardProduction> getLastProductions() {
        if (gypsumBoardReportService.getLastProductions().isEmpty()) {
            return null;
        } else return gypsumBoardReportService.getLastProductions();
    }

}
