package ru.artemmaklashev.telegram_bot_raso.components.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;

public interface Command {
    Object execute(Update update) throws IOException;
}
