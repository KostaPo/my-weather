package ru.kostapo.myweather.model.service;

import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.model.User;
import ru.kostapo.myweather.repository.SessionRepository;
import ru.kostapo.myweather.utils.PropertiesUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class SessionServiceImpl implements SessionService {

    private final Duration SESSION_TTL;
    private final SessionRepository sessionRepository;

    public SessionServiceImpl() {
        long ttlMin = Long.parseLong(PropertiesUtil.getProperty("session_ttl"));
        SESSION_TTL = Duration.ofMinutes(ttlMin);
        this.sessionRepository = new SessionRepository();
    }

    @Override
    public Optional<Session> findById(String id) {
        return sessionRepository.findByKey(id);
    }

    @Override
    public void deleteById(String id) {
        sessionRepository.deleteByKey(id);
    }

    @Override
    public void deleteAllExpired(LocalDateTime current) {
        sessionRepository.deleteExpiredSessions(current);
    }

    @Override
    public Session newSessionForUser(User user) {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plus(SESSION_TTL));
        return session;
    }

    @Override
    public Session save(Session session) {
        return sessionRepository.save(session);
    }
}
