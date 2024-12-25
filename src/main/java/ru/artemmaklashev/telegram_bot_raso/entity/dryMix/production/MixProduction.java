package ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.Shift;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.DryMix;
import ru.artemmaklashev.telegram_bot_raso.entity.production.Productions;


import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "mix_production")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mix_production")
public class MixProduction extends Productions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "production_start")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime productionStart;

    @Column(name = "production_finish")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private LocalDateTime productionFinish;

    @Column(name = "production_date")
    private LocalDateTime productionDate;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @ManyToOne
    @JoinColumn(name = "mix_id")
    private DryMix mix;

    public void updateProductionDate() {
        if (productionStart != null) {
            this.productionDate = productionStart.getHour() < 4 ? productionStart.minusDays(1L).toLocalDate().atStartOfDay() : productionStart.toLocalDate().atStartOfDay();
        }
    }

}
