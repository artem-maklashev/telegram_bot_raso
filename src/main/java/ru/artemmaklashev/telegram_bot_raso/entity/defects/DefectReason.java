package ru.artemmaklashev.telegram_bot_raso.entity.defects;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "defect_reason")
public class DefectReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;
}
