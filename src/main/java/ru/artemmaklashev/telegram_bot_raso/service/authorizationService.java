package ru.artemmaklashev.telegram_bot_raso.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.artemmaklashev.telegram_bot_raso.controller.AuthorizationController;

@Service
public class authorizationService {

    private final AuthorizationController controller;

    public authorizationService(AuthorizationController controller) {
        this.controller = controller;
    }
    public String getAuthorizationToken() {
        return controller.getToken();
    }
}
