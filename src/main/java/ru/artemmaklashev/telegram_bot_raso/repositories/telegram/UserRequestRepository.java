package ru.artemmaklashev.telegram_bot_raso.repositories.telegram;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.artemmaklashev.telegram_bot_raso.entity.request.UserRequest;

import java.time.ZonedDateTime;
import java.util.List;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
    List<UserRequest> findByUserId(Long userId);
    List<UserRequest> findByCreatedAtAfter(ZonedDateTime date);
}
