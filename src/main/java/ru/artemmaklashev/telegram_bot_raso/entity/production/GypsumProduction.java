package ru.artemmaklashev.telegram_bot_raso.entity.production;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsum.Gypsum;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gypsum_production")
public class GypsumProduction extends Production {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "production_log_id")
    private ProductionList productionList;

    @ManyToOne
    @JoinColumn(name = "gypsum_id")
    private Gypsum product;

    @ManyToOne
    @JoinColumn(name = "gypsum_category_id")
    private GypsumCategory category;

    @Column(name = "value")
    private Float value;
}
