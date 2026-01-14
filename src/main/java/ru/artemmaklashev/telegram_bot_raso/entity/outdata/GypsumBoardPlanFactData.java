package ru.artemmaklashev.telegram_bot_raso.entity.outdata;

import lombok.Data;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;

import java.sql.Date;

@Data
public class GypsumBoardPlanFactData {
    private Date date;
    private float planValue;
    private float factValue;
    private GypsumBoard gypsumBoard;

    public GypsumBoardPlanFactData(Date date, float planValue, float factValue, GypsumBoard gypsumBoard) {
        this.date = date;
        this.planValue = planValue;
        this.factValue = factValue;
        this.gypsumBoard = gypsumBoard;
    }
}
