package ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.defects.Defects;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "board_defects_log")
public class BoardDefectsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "production_log_id")
    private ProductionList productionList;

    @Column(name = "value")
    private double value;

    @ManyToOne
    @JoinColumn(name = "defects_id")
    private Defects defects;
}
