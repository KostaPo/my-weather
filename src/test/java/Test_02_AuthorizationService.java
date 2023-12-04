import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.*;
import ru.kostapo.myweather.exception.PasswordMismatchException;
import ru.kostapo.myweather.exception.UniqConstraintViolationException;
import ru.kostapo.myweather.exception.UserNotFoundException;
import ru.kostapo.myweather.exception.ValidConstraintViolationException;
import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.model.User;
import ru.kostapo.myweather.model.dao.SessionDAO;
import ru.kostapo.myweather.model.dao.UserDAO;
import ru.kostapo.myweather.model.dto.UserReqDto;
import ru.kostapo.myweather.servlets.servlet.service.AuthorizationService;
import util.TestHibernateUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_02_AuthorizationService {

    private static SessionFactory sessionFactory;
    private static UserDAO userDAO;
    private static SessionDAO sessionDAO;
    private static AuthorizationService authService;

    @BeforeAll
    public static void init() {
        sessionFactory = TestHibernateUtil.getSessionFactory();
        authService = new AuthorizationService(sessionFactory);
        userDAO = new UserDAO(sessionFactory);
        sessionDAO = new SessionDAO(sessionFactory);
    }

    @BeforeEach
    public void clearTables() {
        String clearUsersHQL = "Delete from User";
        String clearSessionsHQL = "Delete from Session";
        try (org.hibernate.Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery(clearSessionsHQL).executeUpdate();
            session.createQuery(clearUsersHQL).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new HibernateException("Database error!", e);
        }
    }

    @Test
    @Order(1)
    @DisplayName("Успешная регистрация")
    public void test01_AuthRegistration() {
        UserReqDto testUserRequest = UserReqDto.builder()
                .login("testUsr")
                .password("testPwd")
                .build();
        Session tmpSession = authService.registration(testUserRequest);
        User tmpUser = tmpSession.getUser();

        assertNotNull(tmpSession);
        assertNotNull(tmpUser);

        Optional<Session> persistSession = sessionDAO.findById(tmpSession.getId());
        Optional<User> persistUser = userDAO.findByLogin(tmpUser.getLogin());

        assertTrue(persistSession.isPresent());
        assertTrue(persistUser.isPresent());

        assertEquals(tmpUser.getId(), persistUser.get().getId());
    }

    @Test
    @Order(2)
    @DisplayName("Регистрация НЕ УНИКАЛЬНОГО юзера даст exception")
    public void test02_AuthNonUniqRegistration() {
        UserReqDto testUserRequest = UserReqDto.builder()
                .login("testUsr")
                .password("testPwd")
                .build();
        authService.registration(testUserRequest);

        assertThrows(UniqConstraintViolationException.class,
                () -> authService.registration(testUserRequest));
    }

    @Test
    @Order(3)
    @DisplayName("Регистрация НЕ ВАЛИДНОГО юзера даст exception")
    public void test03_AuthNonValidRegistration() {
        UserReqDto testUserRequest = UserReqDto.builder()
                .login("u")
                .password("u")
                .build();

        assertThrows(ValidConstraintViolationException.class,
                () -> authService.registration(testUserRequest));
    }

    @Test
    @Order(4)
    @DisplayName("Успешный логин")
    public void test04_AuthLogin() {
        UserReqDto testUserRequest = UserReqDto.builder()
                .login("testUsr")
                .password("testPwd")
                .build();
        authService.registration(testUserRequest);

        Session tmpSession = authService.login(testUserRequest);
        User tmpUser = tmpSession.getUser();

        assertNotNull(tmpSession);
        assertNotNull(tmpUser);
    }

    @Test
    @Order(5)
    @DisplayName("Неверный login при входе дает exception")
    public void test05_AuthLoginFail() {
        UserReqDto testUserRequest = UserReqDto.builder()
                .login("testUsr")
                .password("testPwd")
                .build();
        authService.registration(testUserRequest);

        UserReqDto testUserRequest2 = UserReqDto.builder()
                .login("wrong_login")
                .password("testPwd")
                .build();

        assertThrows(UserNotFoundException.class,
                () -> authService.login(testUserRequest2));
    }

    @Test
    @Order(6)
    @DisplayName("Неверный password при входе дает exception")
    public void test06_AuthPasswordFail() {
        UserReqDto testUserRequest = UserReqDto.builder()
                .login("testUsr")
                .password("testPwd")
                .build();
        authService.registration(testUserRequest);

        UserReqDto testUserRequest2 = UserReqDto.builder()
                .login("testUsr")
                .password("wrong_password")
                .build();

        assertThrows(PasswordMismatchException.class,
                () -> authService.login(testUserRequest2));
    }

    @Test
    @Order(7)
    @DisplayName("TTL каждой сессии 30 минут")
    public void test07_AuthSessionTTL() {
        UserReqDto testUserRequest = UserReqDto.builder()
                .login("testUsr")
                .password("testPwd")
                .build();
        Session session = authService.registration(testUserRequest);
        Duration difference = Duration.between(LocalDateTime.now(), session.getExpiresAt());
        long differenceMinutes = difference.toMinutes();
        long expectedDifference = 30;

        // Проверка с погрешностью в 1 минуту из-за не атомарности
        assertEquals(expectedDifference, differenceMinutes, 1);
    }

    @Test
    @Order(8)
    @DisplayName("Удаление сессий старше 30 минут")
    public void test08_AuthDeleteExpiredSessions() {
        UserReqDto testUserRequest = UserReqDto.builder()
                .login("testUsr")
                .password("testPwd")
                .build();
        Session session = authService.registration(testUserRequest);
        authService.deleteExpiredSessions(LocalDateTime.now().plusMinutes(30));
        Optional<Session> persistSession = sessionDAO.findById(session.getId());

        assertFalse(persistSession.isPresent());
    }
}