package ru.artemmaklashev.telegram_bot_raso.entity.delays;



import ru.artemmaklashev.telegram_bot_raso.entity.Product;
import ru.artemmaklashev.telegram_bot_raso.entity.Shift;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Delays <T extends Product> {
    private int  id;


    private LocalDate delayDate;


    private LocalDateTime startTime;


    private LocalDateTime endTime;


    private UnitPart unitPart;


    private Shift shift;


    private T product;

    private DelayType delayType;
}
