package ru.artemmaklashev.telegram_bot_raso.entity.defects;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "defects")
public class Defects {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "defect_types_id")
    private DefectTypes defectTypes;

    @ManyToOne
    @JoinColumn(name = "defect_reason_id")
    private DefectReason defectReason;
}
