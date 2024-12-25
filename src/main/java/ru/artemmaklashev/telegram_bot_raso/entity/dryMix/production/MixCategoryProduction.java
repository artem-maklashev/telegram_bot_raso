package ru.artemmaklashev.telegram_bot_raso.entity.dryMix.production;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mix_category_production")
public class MixCategoryProduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mix_production_id")
    private MixProduction production;

    @ManyToOne
    @JoinColumn(name = "mix_category_id")
    private MixCategory category;

    @Column(name = "quantity")
    private float quantity;

}
