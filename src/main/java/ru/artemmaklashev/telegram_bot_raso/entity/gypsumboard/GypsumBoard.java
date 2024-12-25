package ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.artemmaklashev.telegram_bot_raso.entity.Product;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "gypsum_board")
//@DiscriminatorValue("GypsumBoard")
public class GypsumBoard extends Product {
    @ManyToOne
    @JoinColumn(name = "board_types_id")
    private BoardType boardType;

    @ManyToOne
    @JoinColumn(name = "edge_id")
    private Edge edge;

    @ManyToOne
    @JoinColumn(name = "thickness_id")
    private Thickness thickness;

    @ManyToOne
    @JoinColumn(name = "width_id")
    private Width width;

    @ManyToOne
    @JoinColumn(name = "length_id")
    private Length length;

    @Override
    public String toString() {
        return  getPType().getName() + " " +
                getTradeMark().getName() + " тип " +
                boardType.getName() + "-" +
                edge.getName() + " " +
                thickness.getValue() + "-" +
                width.getValue() + "-" +
                length.getValue() ;
    }
}





