package ru.artemmaklashev.telegram_bot_raso.components;

import org.springframework.stereotype.Component;
import ru.artemmaklashev.telegram_bot_raso.components.commands.Command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CallbackCommandRegistry {
    private final Map<String, Command> commandMap = new HashMap<>();

    public CallbackCommandRegistry(List<Command> commands) {
        for (Command command : commands) {
            Component annotation = command.getClass().getAnnotation(Component.class);
            if (annotation != null && !annotation.value().isEmpty())  {
                commandMap.put(annotation.value().toLowerCase(), command);
            }
        }
    }

    public Command getCommand(String key) {
        return commandMap.get(key.toLowerCase());
    }
}
