package ru.artemmaklashev.telegram_bot_raso.entity.production;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.artemmaklashev.telegram_bot_raso.entity.Shift;

import java.time.LocalDateTime;

public abstract class Productions {
    private long id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime productionStart;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime productionFinish;


    private LocalDateTime productionDate;


    private Shift shift;
    public void  setProductionDate() {
        if (this.productionStart!= null && this.productionFinish!= null) {
            if (this.productionStart.getHour() < 4) {
                this.productionDate = this.productionStart.minusDays(1).toLocalDate().atStartOfDay();
            }
            else {
                this.productionDate = this.productionStart.toLocalDate().atStartOfDay();
            }
        }
        System.out.println("Полученное вермя начала производства:" + this.productionStart);
        System.out.println("Преобразованная дата производства:" + this.productionDate);
    }

}
