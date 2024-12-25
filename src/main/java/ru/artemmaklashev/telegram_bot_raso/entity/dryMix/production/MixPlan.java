package ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.dryMix.DryMix;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mix_plan")
public class MixPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "plan_date")
    private LocalDate planDate;

    @ManyToOne
    @JoinColumn(name = "dry_mix_id")
    private DryMix dryMix;

    @Column(name = "value")
    private Long value;
}
