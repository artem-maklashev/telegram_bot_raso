package ru.artemmaklashev.telegram_bot_raso.entity.request;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "user_request")
@Data
public class UserRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "text")
    private String text;

    @Column(name = "command")
    private String command;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();
}
