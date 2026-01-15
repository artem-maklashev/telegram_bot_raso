package ru.artemmaklashev.telegram_bot_raso.components.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.GypsumBoardPlanFactData;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.IntervalData;
import ru.artemmaklashev.telegram_bot_raso.service.reportServices.gypsumBoard.GypsymBoardReportService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component("gypsumBoardPlanTable")
public class GypsumBoardPlanTableCommand implements Command{
    private final GypsymBoardReportService service;
    public GypsumBoardPlanTableCommand(GypsymBoardReportService service) {
        this.service = service;
    }

    @Override
    public Object execute(Update update) throws IOException {
//        var data = service.getIntervalData(LocalDate.now());
        var data = service.getIntervalData(LocalDate.ofYearDay(2026, 1)); //TODO: заглушка
        var tableData = composeTableData(data);
        var table = service.buildTable(tableData);
        Long chatId = update.getMessage().getChatId();
        byte[] bytes = service.buildExcelFile(table);
        InputFile inputFile = new InputFile(
                new ByteArrayInputStream(bytes),
                "plan_fact.xlsx"
        );
        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .caption("Таблица плана-факт")
                .build();
    }


    private List<GypsumBoardPlanFactData> composeTableData(IntervalData  intervalData) {
        var productions = intervalData.getProductions();
        var plans = intervalData.getPlans();
        return service.getDataForTable(plans,  productions);
    }
}
