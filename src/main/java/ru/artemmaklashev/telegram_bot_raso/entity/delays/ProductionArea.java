package ru.artemmaklashev.telegram_bot_raso.entity.delays;

import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
@Table(name = "production_area")
public class ProductionArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "division_id")
    private Division division;
}
