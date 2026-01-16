package ru.artemmaklashev.telegram_bot_raso.entity.outdata.dto;

import lombok.Data;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.outdata.PlanFactValues;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
public class GypsumBoardRow {
    private GypsumBoard gypsumBoard;
    private Map<LocalDate, PlanFactValues> valuesByDate = new HashMap<>();
}
