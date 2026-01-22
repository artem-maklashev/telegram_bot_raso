package ru.artemmaklashev.telegram_bot_raso.service.telegram;

import org.springframework.stereotype.Service;
import ru.artemmaklashev.telegram_bot_raso.entity.request.UserRequest;
import ru.artemmaklashev.telegram_bot_raso.repositories.telegram.UserRequestRepository;

import java.time.ZonedDateTime;

@Service
public class RequestLoggingService {
    private final UserRequestRepository repository;

    public RequestLoggingService(UserRequestRepository repository) {
        this.repository = repository;
    }

    public void logRequest(Long userId, Long chatId, String text, String Command) {
        UserRequest request = new UserRequest();
        request.setUserId(userId);
        request.setChatId(chatId);
        request.setText(text);
        request.setCommand(Command);
        request.setCreatedAt(ZonedDateTime.now());

        repository.save(request);
    }
}
