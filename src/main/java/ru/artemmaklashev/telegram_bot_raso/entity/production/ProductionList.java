package ru.artemmaklashev.telegram_bot_raso.entity.production;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.artemmaklashev.telegram_bot_raso.entity.Shift;
import ru.artemmaklashev.telegram_bot_raso.entity.Types;


import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "productionlog")
public class ProductionList extends Productions{
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
    @JoinColumn(name = "product_types_id")
    private Types type;


    public void updateProductionDate() {
        if (productionStart != null) {
            this.productionDate = productionStart.getHour() < 4 ? productionStart.minusDays(1L).toLocalDate().atStartOfDay() : productionStart.toLocalDate().atStartOfDay();
        }
    }


    @Override
    public String toString() {
        return "ProductionList{" +
                "id=" + id +
                ", productionDate=" + productionDate +
                ", shift=" + shift +
                '}';
    }
}
