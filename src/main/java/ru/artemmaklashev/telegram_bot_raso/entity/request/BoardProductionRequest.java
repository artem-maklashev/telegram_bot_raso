package ru.artemmaklashev.telegram_bot_raso.entity.request;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;

import java.util.List;

@Data
public class BoardProductionRequest {

    @Value("${spring.jackson.time-zone}")
    private String timeZone;


//    @Getter
    private List<GypsumBoard> gypsumBoards;
//    @Setter
    private String startDate;
//    @Setter
    private String endDate;

    // Геттеры и сеттеры

//    public LocalDateTime getStartDate() {
//        return stringDateToLocalDateTime(startDate);
//    }
//
//    public LocalDateTime getEndDate() {
//        return stringDateToLocalDateTime(endDate);
//    }
//
//    private LocalDateTime stringDateToLocalDateTime(String date) {
//        Instant instant = Instant.parse(date);
//        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of(timeZone));
//        return zonedDateTime.toLocalDate().atStartOfDay();
//    }
}
