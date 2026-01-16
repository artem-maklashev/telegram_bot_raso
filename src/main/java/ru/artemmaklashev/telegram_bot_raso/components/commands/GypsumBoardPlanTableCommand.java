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

        if (!update.hasCallbackQuery()) {
            return null;
        }

        var callback = update.getCallbackQuery();

        if (callback.getMessage() == null) {
            throw new IllegalStateException("Callback without message (inline mode)");
        }

        Long chatId = callback.getMessage().getChatId();

        var data = service.getIntervalData(LocalDate.now());
        var tableData = composeTableData(data);
        var table = service.buildTable(tableData);

        byte[] bytes = service.buildExcelFile(table);
        System.out.println("Table created, size: " + bytes.length);

        InputFile inputFile = new InputFile(
                new ByteArrayInputStream(bytes),
                "plan_fact.xlsx"
        );
        System.out.println("Excel file created, size: " + bytes.length);

        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .caption("Таблица план-факт на утро " + LocalDate.now())
                .build();
    }




    private List<GypsumBoardPlanFactData> composeTableData(IntervalData  intervalData) {
        var productions = intervalData.getProductions();
        var plans = intervalData.getPlans();
        return service.getDataForTable(plans,  productions);
    }
}
