package ru.artemmaklashev.telegram_bot_raso.entity.gypsumboard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plan")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "plan_date")
    private LocalDate planDate;

    @ManyToOne
    @JoinColumn(name = "gypsum_board_id")
    private GypsumBoard gypsumBoard;

    @Column(name = "value")
    private float planValue;
}
