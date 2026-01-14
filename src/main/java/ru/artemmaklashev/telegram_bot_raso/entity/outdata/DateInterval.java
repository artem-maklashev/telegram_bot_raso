package ru.artemmaklashev.telegram_bot_raso.entity.outdata;

import java.time.LocalDate;

public class DateInterval {
    private final LocalDate start;
    private final LocalDate end;

    public DateInterval(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must not be after end date");
        }
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }
}

