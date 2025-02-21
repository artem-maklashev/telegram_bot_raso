package ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production.MixProduction;


import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mix_delays")
public class MixDelay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "delay_type_id")
//    private DelayType delayType;

    @ManyToOne
    @JoinColumn(name = "mix_production_id")
    private MixProduction mixProduction;

    @Column(name = "delay_start")
    private LocalDateTime delayStart;

    @Column(name = "delay_end")
    private LocalDateTime delayEnd;

    @ManyToOne
    @JoinColumn(name = "mix_unit_part_id")
    private MixUnitPart mixUnitPart;

    public Long getDuration() {
        return Duration.between(delayStart, delayEnd).getSeconds()/60;
    }

}
