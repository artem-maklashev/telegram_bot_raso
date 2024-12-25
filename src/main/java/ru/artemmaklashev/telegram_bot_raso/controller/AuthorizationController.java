package ru.artemmaklashev.telegram_bot_raso.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.artemmaklashev.telegram_bot_raso.model.AuthenticationResponse;

import java.net.URI;

@Controller
public class AuthorizationController {
    @Value("${backend.auth.url}")
    private String authUrl;

    public RestTemplate restTemplate;

    public AuthorizationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getToken() {
        URI uri = UriComponentsBuilder.fromUriString(authUrl + "/auth/authenticate").build().toUri();
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(uri);
        builder.queryParam("email", "telegram");
        builder.queryParam("password", "402986");
        URI finalUri = builder.build().toUri();
        AuthenticationResponse result = restTemplate.getForObject(finalUri, AuthenticationResponse.class);
        if (result != null) {
            return result.getToken();
        }
        return null;
    }
}
