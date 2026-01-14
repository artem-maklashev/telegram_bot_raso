package ru.artemmaklashev.telegram_bot_raso.components.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.artemmaklashev.telegram_bot_raso.service.reportServices.gypsumBoard.GypsymBoardReportService;

import java.time.LocalDate;

@Component("gypsumBoardPlanTable")
public class GypsumBoardPlanTableCommand implements Command{
    private final GypsymBoardReportService service;
    public GypsumBoardPlanTableCommand(GypsymBoardReportService service) {
        this.service = service;
    }

    @Override
    public Object execute(Update update) {
        var data = service.getIntervalData(LocalDate.now());

        return null;
    }
}
