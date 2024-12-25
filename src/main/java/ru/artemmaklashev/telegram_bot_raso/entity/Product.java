package ru.artemmaklashev.telegram_bot_raso.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "types_id")
    private Types pType;

    @ManyToOne
    @JoinColumn(name = "trade_mark_id")
    private TradeMark tradeMark;
}

