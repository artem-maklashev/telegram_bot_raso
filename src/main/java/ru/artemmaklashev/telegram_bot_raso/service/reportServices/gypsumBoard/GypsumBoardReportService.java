package ru.artemmaklashev.telegram_bot_raso.service.reportServices.gypsumBoard;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.BoardDelays;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.Plan;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.*;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.dto.GypsumBoardRow;
import ru.artemmaklashev.telegram_bot_raso.entity.production.BoardProduction;
import ru.artemmaklashev.telegram_bot_raso.repositories.delays.BoardsDelaysRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard.BoardProductionRepository;
import ru.artemmaklashev.telegram_bot_raso.repositories.gypsumboard.PlanRepository;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GypsumBoardReportService {
    private final BoardProductionRepository productionRepository;
    private final BoardsDelaysRepository delaysRepository;
    private final PlanRepository planRepository;


    public GypsumBoardReportService(BoardProductionRepository productionRepository, BoardsDelaysRepository delaysRepository, PlanRepository planRepository) {
        this.productionRepository = productionRepository;
        this.delaysRepository = delaysRepository;
        this.planRepository = planRepository;
    }

    public List<BoardProduction> getLastProductions() {
        LocalDateTime date = LocalDate.now().minusDays(1L).atStartOfDay();
        System.out.println("Запрашиваю данные за : " + date);
        List<BoardProduction> result = productionRepository.findByProductionListProductionDateBetween(date, date);
        System.out.println("Получены данные в размере: " + result.size());
        return result;
    }

    public List<BoardDelays> getLastDelays() {
        LocalDate date = LocalDate.now().minusDays(1L);
        return delaysRepository.findAllByDelayDateBetween(date, date);
    }

    public List<GypsumBoardProductionData> getProductionsByInterval() {
        // 1. Определение временного интервала
        var data = getIntervalData(LocalDate.now());

        // 3. Обработка данных
        Map<Integer, GypsumBoardProductionData> result = processProductions(data.getProductions());
        processPlans(data.getPlans(), result);

        // 4. Расчет и возврат результата
        return result.values().stream().peek(this::calculateDefectiveValue).sorted(Comparator.comparingDouble(GypsumBoardProductionData::getPlanValue).reversed()).collect(Collectors.toList());
    }

    private void calculateDefectiveValue(GypsumBoardProductionData item) {
        double defectiveValue = (item.getTotal() != 0) ? (1 - item.getFactValue() / item.getTotal()) : 0;
        item.setDefectiveValue((float) defectiveValue);
    }

    // Получение отфильтрованного списка производств
    private List<BoardProduction> getFilteredProductions(LocalDateTime startDate, LocalDateTime endDate) {
        return productionRepository.findByProductionListProductionDateBetween(startDate, endDate).stream().filter(p -> p.getCategory() != null && p.getCategory().getId() >= 1 && p.getCategory().getId() < 5).toList();
    }

    // Обработка данных производства
    private Map<Integer, GypsumBoardProductionData> processProductions(List<BoardProduction> productions) {
        Map<Integer, GypsumBoardProductionData> result = new HashMap<>();

        for (BoardProduction production : productions) {
            if (production.getProduct() == null) continue;

            int productId = production.getProduct().getId();
            String title = getGypsumBoardName(production.getProduct());

            float total = production.getCategory().getId() == 1 ? production.getValue() : 0;
            float fact = production.getCategory().getId() != 1 ? production.getValue() : 0;

            GypsumBoardProductionData data = result.computeIfAbsent(productId, k -> new GypsumBoardProductionData(title, 0, 0, 0, 0));

            data.addValues(0, total, fact, 0); // Обновляем значения
        }


        return result;
    }

    // Обработка данных плана
    private void processPlans(List<Plan> plans, Map<Integer, GypsumBoardProductionData> result) {
        for (Plan plan : plans) {
            if (plan.getGypsumBoard() == null) continue;

            int boardId = plan.getGypsumBoard().getId();
            String title = getGypsumBoardName(plan.getGypsumBoard());

            GypsumBoardProductionData data = result.computeIfAbsent(boardId, k -> new GypsumBoardProductionData(title, 0, 0, 0, 0));

            data.addValues(plan.getPlanValue(), 0, 0, 0);
        }
    }

    private String getGypsumBoardName(GypsumBoard gb) {
        return gb.getTradeMark().getName() + " " + gb.getBoardType().getName() + "-" + gb.getEdge().getName() + " " + gb.getThickness().getValue() + "-" + gb.getWidth().getValue() + "-" + gb.getLength().getValue();
    }


    public static DateInterval determineReportingInterval(LocalDate today) {
        LocalDate endDate = today.minusDays(1); // Вчера

        LocalDate startDate;
        if (today.getDayOfMonth() != 1) {
            startDate = today.withDayOfMonth(1); // Начало текущего месяца
        } else {
            startDate = today.minusMonths(1).withDayOfMonth(1); // Начало предыдущего месяца
        }

        return new DateInterval(startDate, endDate);
    }

    public IntervalData getIntervalData(LocalDate today) {
        DateInterval interval = determineReportingInterval(today);

        List<BoardProduction> productions = getFilteredProductions(interval.getStart().atStartOfDay(), interval.getEnd().atStartOfDay());

        List<Plan> plans = planRepository.findAllByPlanDateBetween(interval.getStart(), interval.getEnd());

        return new IntervalData(productions, plans);
    }

    public List<GypsumBoardPlanFactData> getDataForTable(List<Plan> plans, List<BoardProduction> productions) {
        Map<PlanFactKey, GypsumBoardPlanFactData> map = new HashMap<>();

        // планы
        for (Plan plan : plans) {
            LocalDate date = plan.getPlanDate();
            Integer boardId = plan.getGypsumBoard().getId();

            map.computeIfAbsent(new PlanFactKey(date, boardId), k -> new GypsumBoardPlanFactData(date, 0, 0, plan.getGypsumBoard())).getPlanFactValues().setPlanValue(plan.getPlanValue());
        }

        // факты (категории 2 и 3)
        for (BoardProduction production : productions) {
            int categoryId = production.getCategory().getId();
            if (categoryId != 2 && categoryId != 3) {
                continue;
            }

            LocalDate date = production.getProductionList().getProductionDate().toLocalDate();
            Integer boardId = production.getProduct().getId();

            map.computeIfAbsent(new PlanFactKey(date, boardId), k -> new GypsumBoardPlanFactData(date, 0, 0, production.getProduct())).getPlanFactValues().setFactValue(map.get(new PlanFactKey(date, boardId)).getPlanFactValues().getFactValue() + production.getValue());
        }

        return new ArrayList<>(map.values());
    }


    private record PlanFactKey(LocalDate date, Integer boardId) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PlanFactKey key)) return false;
            return date.equals(key.date) && boardId.equals(key.boardId);
        }

    }

    public PlanFactTableData buildTable(List<GypsumBoardPlanFactData> source) {

        // уникальные даты (колонки)
        List<LocalDate> dates = source.stream().map(GypsumBoardPlanFactData::getDate).distinct().sorted().toList();

        // строки по виду гипсокартона
        Map<Integer, GypsumBoardRow> rows = new LinkedHashMap<>();

        for (GypsumBoardPlanFactData item : source) {

            GypsumBoardRow row = rows.computeIfAbsent(item.getGypsumBoard().getId(), id -> {
                GypsumBoardRow r = new GypsumBoardRow();
                r.setGypsumBoard(item.getGypsumBoard());
                return r;
            });

            row.getValuesByDate().put(item.getDate(), item.getPlanFactValues());
        }

        return new PlanFactTableData(dates, new ArrayList<>(rows.values()));
    }

    public void exportToExcel(PlanFactTableData table, OutputStream out) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("План-Факт");

            // Создаём стиль с переносом текста один раз
            CellStyle wrapStyle = workbook.createCellStyle();
            wrapStyle.setWrapText(true);

            int rowIdx = 0;

            // ===== HEADER =====
            Row header = sheet.createRow(rowIdx++);
            int colIdx = 0;
            header.createCell(colIdx++).setCellValue("Гипсокартон");

            for (LocalDate date : table.getDates()) {
                Cell cell = header.createCell(colIdx++);
                cell.setCellValue(date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                cell.setCellStyle(wrapStyle); // опционально, если дата длинная
            }
            header.createCell(colIdx++).setCellValue("План");
            header.createCell(colIdx++).setCellValue("Факт");
            header.createCell(colIdx++).setCellValue("Δ");
            if (!table.getDates().isEmpty()) {
                // ===== DATA =====
                for (GypsumBoardRow rowData : table.getRows()) {
                    Row row = sheet.createRow(rowIdx++);
                    row.setHeightInPoints(50); // обеспечиваем место для двух строк
                    colIdx = 0;

                    row.createCell(colIdx++).setCellValue(rowData.getGypsumBoard().toString());
                    float totalPlan = 0;
                    float totalProduction = 0;

                    for (LocalDate date : table.getDates()) {
                        PlanFactValues v = rowData.getValuesByDate().get(date);

                        if (v != null) {
                            float deviation = v.getFactValue() - v.getPlanValue();
                            String cellValue = String.format("План: %.0f \nФакт: %.0f \nΔ: %.0f", v.getPlanValue(), v.getFactValue(), deviation);
                            Cell cell = row.createCell(colIdx++);
                            cell.setCellValue(cellValue);
                            cell.setCellStyle(wrapStyle);
                            totalPlan += v.getPlanValue();
                            totalProduction += v.getFactValue();
                        } else {
                            row.createCell(colIdx++).setCellValue("");
                        }
                    }
                    row.createCell(colIdx++).setCellValue(totalPlan);
                    row.createCell(colIdx++).setCellValue(totalProduction);
                    row.createCell(colIdx++).setCellValue(totalProduction - totalPlan);
                }
            } else {
                if (table.getRows().isEmpty()) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue("Нет данных");
                }
            }
            // автоширина
            for (int i = 0; i <= table.getDates().size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
        }
    }


    public byte[] buildExcelFile(PlanFactTableData table) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            exportToExcel(table, out);
            return out.toByteArray();
        }
    }


}
