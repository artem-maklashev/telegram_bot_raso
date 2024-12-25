package ru.artemmaklashev.telegram_bot_raso.entity.delays;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.artemmaklashev.telegram_bot_raso.entity.Shift;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "delays")
public class BoardDelays extends Delays<GypsumBoard> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int  id;

    @Column(name = "delay_date")
    private LocalDate delayDate;

    @Column(name = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "unit_part_id")
    private UnitPart unitPart;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @ManyToOne
    @JoinColumn(name = "gypsum_board_id")
    private GypsumBoard product;

    @ManyToOne
    @JoinColumn(name = "delay_type_id")
    private DelayType delayType;

    public void setDelayDate() {
        if (startTime.getHour() < 4) {
            delayDate = startTime.toLocalDate().minusDays(1);
        } else {
            delayDate = startTime.toLocalDate();
        }
    }

    public Long getDuration() {
        return Duration.between(startTime, endTime).getSeconds()/60;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        BoardDelays that = (BoardDelays) o;
//        return Objects.equals(id, that.id) &&
//                Objects.equals(delayDate, that.delayDate) &&
//                Objects.equals(shift, that.shift) &&
//                Objects.equals(product, that.product);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, delayDate, shift, product);
//    }
}
