package ru.artemmaklashev.telegram_bot_raso.entity.specification;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.production.ProductionList;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "material_consumption")
public class MaterialConsumption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "production_list_id")
    private ProductionList productionList;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(name = "quantity")
    private Double quantity;

    @Override
    public String toString() {
        return "MaterialConsumption{" +
                "id=" + id +
                ", productionList=" + productionList +
                ", material=" + material +
                ", quantity=" + quantity +
                '}';
    }
}
