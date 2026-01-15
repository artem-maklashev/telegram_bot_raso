package ru.artemmaklashev.telegram_bot_raso.entity.outdata;

import ru.artemmaklashev.telegram_bot_raso.entity.outdata.dto.GypsumBoardRow;

import java.time.LocalDate;
import java.util.List;

public class PlanFactTableData {
    private final List<LocalDate> dates;
    private final List<GypsumBoardRow> rows;

    public PlanFactTableData(
            List<LocalDate> dates,
            List<GypsumBoardRow> rows
    ) {
        this.dates = dates;
        this.rows = rows;
    }

    public List<LocalDate> getDates() {
        return dates;
    }

    public List<GypsumBoardRow> getRows() {
        return rows;
    }
}
