package ru.artemmaklashev.telegram_bot_raso.entity.dryMix.delays;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mix_unit")
public class MixUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "production_area_id")
    private MixProductionArea productionArea;

    @Column(name = "name")
    private String name;
}
