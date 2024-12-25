package ru.artemmaklashev.telegram_bot_raso.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor


public class AuthenticationResponse {
    private String token;

    public String getToken() {
        return token;
    }
}