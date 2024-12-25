package ru.artemmaklashev.telegram_bot_raso.entity.specification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard.GypsumBoard;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "specifications")
/**
 * Спецификация
 */
public class Specification {
    /**
     * id спецификации
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    /**
     * название спецификации
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private GypsumBoard product;
    /**
     * материал
     */
    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;
    /**
     * количество материала
     */
    private Float quantity;
}
