package ru.artemmaklashev.telegram_bot_raso.entity.production;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoardCategory;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "board_production")
public class BoardProduction extends Production<GypsumBoardCategory, GypsumBoard> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "production_log_id")
    private ProductionList productionList;

    @ManyToOne
    @JoinColumn(name = "gypsum_board_id")
    private GypsumBoard product;

    @ManyToOne
    @JoinColumn(name = "gboard_category_id")
    private GypsumBoardCategory category;

    @Column(name = "value")
    private Float value;

}
