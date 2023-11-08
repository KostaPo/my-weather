package ru.kostapo.myweather.service;

import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionService {
    Optional<Session> findById(String id);
    void deleteById(String id);
    void deleteAllExpired(LocalDateTime current);
    Session newSessionForUser(User user);
    Session save(Session session);
}
