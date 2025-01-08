package ru.artemmaklashev.telegram_bot_raso.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.artemmaklashev.telegram_bot_raso.service.gypsumboard.TelegramNotificationSender;

@RestController
@RequestMapping("/telegram")

public class TelegramNotificationSenderController {
    private final TelegramNotificationSender telegramNotificationSender;

    public TelegramNotificationSenderController(TelegramNotificationSender telegramNotificationSender) {
        this.telegramNotificationSender = telegramNotificationSender;
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam String message) {
        telegramNotificationSender.sendMessage(message);
        return "Message sent successfully!";
    }

}
