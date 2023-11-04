package ru.kostapo.myweather.service;

import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.model.User;

import java.util.Optional;
import java.util.UUID;

public interface SessionService {
    Optional<Session> findById(String id);
    void deleteById(String id);
    Session newSessionForUser(User user);
    Session save(Session session);
}
