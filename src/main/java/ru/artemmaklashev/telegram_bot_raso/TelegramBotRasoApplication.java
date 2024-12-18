package ru.artemmaklashev.telegram_bot_raso;

//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

//@SpringBootApplication
public class TelegramBotRasoApplication {

    public static void main(String[] args) throws TelegramApiException {
//        SpringApplication.run(TelegramBotRasoApplication.class, args);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new Bot());
    }

}
