package ru.artemmaklashev.telegram_bot_raso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;




@SpringBootApplication
@EnableDiscoveryClient
public class TelegramBotRasoApplication {

    public static void main(String[] args) throws TelegramApiException {
//        ApiContextInitializer.init();
        SpringApplication.run(TelegramBotRasoApplication.class, args);
//        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
//        botsApi.registerBot(new Bot());
    }

}
