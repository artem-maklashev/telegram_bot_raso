package ru.artemmaklashev.telegram_bot_raso.entity.delays;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "delay_type")
public class DelayType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;
}
