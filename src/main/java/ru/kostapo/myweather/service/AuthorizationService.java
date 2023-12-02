package ru.kostapo.myweather.service;

import org.hibernate.SessionFactory;
import ru.kostapo.myweather.exception.PasswordMismatchException;
import ru.kostapo.myweather.exception.UserNotFoundException;
import ru.kostapo.myweather.exception.ValidConstraintViolationException;
import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.model.User;
import ru.kostapo.myweather.model.dao.*;
import ru.kostapo.myweather.model.dto.UserReqDto;
import ru.kostapo.myweather.model.mapper.UserMapper;
import ru.kostapo.myweather.utils.PasswordUtil;
import ru.kostapo.myweather.utils.PropertiesUtil;
import ru.kostapo.myweather.utils.ValidationUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public class AuthorizationService {

    private final Duration SESSION_TTL;
    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;

    public AuthorizationService(SessionFactory sessionFactory) {
        long SESSION_TTL_IN_MINUTES = Long.parseLong(PropertiesUtil.getProperty("session_ttl"));
        SESSION_TTL = Duration.ofMinutes(SESSION_TTL_IN_MINUTES);
        userDAO = new UserDAO(sessionFactory);
        sessionDAO = new SessionDAO(sessionFactory);
    }

    public Session login(UserReqDto userRequest) {
        Optional<User> user = userDAO.findByLogin(userRequest.getLogin());
        if (user.isPresent()) {
            if (PasswordUtil.checkPassword(userRequest.getPassword(), user.get().getPassword())) {
                Session newSession = getNewSession(user.get());
                return sessionDAO.save(newSession);
            }
            throw new PasswordMismatchException("Не верный пароль");
        }
        throw new UserNotFoundException("Не верный логин");
    }

    public Session registration(UserReqDto userReqDto) {
        User user = UserMapper.INSTANCE.toModel(userReqDto);
        if (isUserValid(user)) {
            user.setPassword(PasswordUtil.hashPassword(userReqDto.getPassword()));
        }
        Session session = getNewSession(user);
        user.getSessions().add(session);
        User persistUser = userDAO.save(user);
        return persistUser.getSessions().iterator().next();
    }

    public Cookie getNewCookie(Session session) {
        Cookie cookie = new Cookie("session_id", session.getId());
        cookie.setMaxAge((int) (SESSION_TTL.toMinutes() * 60));
        return cookie;
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cleaningCookies = new Cookie("session_id", null);
        cleaningCookies.setMaxAge(0);
        response.addCookie(cleaningCookies);
        request.getSession().invalidate();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_id")) {
                    sessionDAO.deleteById(cookie.getValue());
                }
            }
        }
    }

    public void deleteExpiredSessions(LocalDateTime currentTime) {
        sessionDAO.deleteExpiredSessions(currentTime);
    }

    private Session getNewSession(User user) {
        Session session = new Session();
        session.setUser(user);
        session.setExpiresAt(LocalDateTime.now().plus(SESSION_TTL));
        return session;
    }

    private boolean isUserValid(User user) {
        Set<ConstraintViolation<User>> violations = ValidationUtil.getValidator().validate(user);
        if (!violations.isEmpty()) {
            throw new ValidConstraintViolationException("Нарушение ограничений валидации", violations);
        }
        return true;
    }
}
