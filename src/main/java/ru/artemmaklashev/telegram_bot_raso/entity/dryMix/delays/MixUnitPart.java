package ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.delays.DelayType;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mix_unit_part")
public class MixUnitPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "mix_unit_id")
    private MixUnit unit;

    @ManyToOne
    @JoinColumn(name = "delay_type_id")
    private DelayType delayType;
}
