package ru.artemmaklashev.telegram_bot_raso.entity.outdata;

import lombok.Data;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.Plan;

import java.sql.Date;
import java.time.LocalDate;

@Data
public class GypsumBoardPlanFactData {
    private LocalDate date;
    private GypsumBoard gypsumBoard;
    private PlanFactValues planFactValues;

    public GypsumBoardPlanFactData(LocalDate date, float planValue, float factValue, GypsumBoard gypsumBoard) {
        this.date = date;
        this.planFactValues = new PlanFactValues(planValue, factValue);
        this.gypsumBoard = gypsumBoard;
    }
}
