package ru.kostapo.myweather.service;

import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.model.User;
import ru.kostapo.myweather.repository.SessionRepository;
import ru.kostapo.myweather.utils.PropertiesUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionServiceImpl implements SessionService {

    private final Duration SESSION_TTL;
    private final SessionRepository sessionRepository;

    public SessionServiceImpl() {
        long ttlHours = Long.parseLong(PropertiesUtil.getProperty("session_ttl"));
        SESSION_TTL = Duration.ofHours(ttlHours);
        this.sessionRepository = new SessionRepository();
    }

    @Override
    public Optional<Session> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Session getNewSession(User user) {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plus(SESSION_TTL));
        return sessionRepository.save(session);
    }


}
