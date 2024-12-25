package ru.artemmaklashev.telegram_bot_raso.entity.delays;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "unit_part")
public class UnitPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit unit;
}
