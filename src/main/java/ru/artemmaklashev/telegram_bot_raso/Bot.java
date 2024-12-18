package ru.artemmaklashev.telegram_bot_raso;

import lombok.NoArgsConstructor;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@NoArgsConstructor
public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "RasoBot";
    }

    @Override
    public String getBotToken() {
        return "7764343387:AAFVAfign7eUwlZMfMSGmPgRPgR7n6UX7_c";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();

        System.out.println(user.getFirstName() + " wrote " + msg.getText());
        sendText(id, msg.getText());
    }

    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

}
