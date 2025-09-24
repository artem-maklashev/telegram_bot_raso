package ru.artemmaklashev.telegram_bot_raso.components.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Command {
    Object execute(Update update);
}
