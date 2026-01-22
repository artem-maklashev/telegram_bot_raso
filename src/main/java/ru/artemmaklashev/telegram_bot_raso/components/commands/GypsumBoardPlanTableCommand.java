package ru.artemmaklashev.telegram_bot_raso.components.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artemmaklashev.telegram_bot_raso.components.MessageService;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.GypsumBoardPlanFactData;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.IntervalData;
import ru.artemmaklashev.telegram_bot_raso.service.reportServices.gypsumBoard.GypsumBoardReportService;
import ru.artemmaklashev.telegram_bot_raso.service.telegram.RequestLoggingService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component("gypsumBoardPlanTable")
public class GypsumBoardPlanTableCommand implements Command{
    private final GypsumBoardReportService service;
    private final MessageService messageService;
    private final RequestLoggingService loggingService;

    public GypsumBoardPlanTableCommand(GypsumBoardReportService service, MessageService messageService, RequestLoggingService loggingService) {
        this.service = service;
        this.messageService = messageService;
        this.loggingService = loggingService;
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
        String userName = callback.getFrom().getFirstName() + " "  + callback.getFrom().getLastName();
        logRequest(callback.getFrom().getId(), chatId, userName, "gypsumBoardPlanTable");
        LocalDate today = LocalDate.now();
//        LocalDate today = LocalDate.ofYearDay(2026, 1);


        var data = service.getIntervalData(today);
        var tableData = composeTableData(data);
        var table = service.buildTable(tableData);

        byte[] bytes = service.buildExcelFile(table);
        System.out.println("Table created, size: " + bytes.length);

        InputFile inputFile = new InputFile(
                new ByteArrayInputStream(bytes),
                "plan_fact.xlsx"
        );
        System.out.println("Excel file created, size: " + bytes.length);
        sendAdminMessage("Пользователь " + userName + " запросил Excel таблицу план-факт за период");
        return SendDocument.builder()
                .chatId(chatId)
                .document(inputFile)
                .caption("Таблица план-факт за период " + GypsumBoardReportService.determineReportingInterval(today).getStart() +
                        " - " + GypsumBoardReportService.determineReportingInterval(today).getEnd())
                .build();
    }

    @Override
    public void logRequest(Long userId, Long chatId, String text, String command) {
        CompletableFuture.runAsync(() -> loggingService.logRequest(userId, chatId, text, command));
    }

    @Override
    public void sendAdminMessage(String text) {
        messageService.sendAdminMessage(text);
    }


    private List<GypsumBoardPlanFactData> composeTableData(IntervalData  intervalData) {
        var productions = intervalData.getProductions();
        var plans = intervalData.getPlans();
        return service.getDataForTable(plans,  productions);
    }
}
