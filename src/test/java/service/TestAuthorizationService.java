package service;

import org.h2.tools.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kostapo.myweather.service.AuthorizationService;
import util.TestHibernateUtil;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestAuthorizationService {

    private static Server tcpServer;
    private static SessionFactory sessionFactory;
    private static AuthorizationService authService;


    @BeforeAll
    public static void init() {
        sessionFactory = TestHibernateUtil.getSessionFactory();
        authService = new AuthorizationService(sessionFactory);
        try {
            tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
            tcpServer.start();
        } catch (SQLException e) {
            throw new RuntimeException("can't create tcp server", e);
        }
    }

    @AfterAll
    public static void tearDown() {
        if (tcpServer != null) {
            tcpServer.stop();
            tcpServer.shutdown();
        }
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    @DisplayName("SessionFactory не NULL")
    public void test01_SessionFactoryNotNull() {
        assertNotNull(sessionFactory);
    }

    @Test
    @DisplayName("Подключение к БД через SessionFactory")
    public void test02_ConnectionToDatabase() {
        try (Session session = sessionFactory.openSession()) {
            assertTrue(session.isConnected());
        }
    }

/*@Test
    @DisplayName("Таблица users создана")
    public void test03_TEST() {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'users'";
            NativeQuery<?> query = session.createNativeQuery(sql);
            BigInteger result = (BigInteger) query.uniqueResult();
            assertEquals(1, result.intValue());
        }
    }

    @Test
    @DisplayName("Таблица sessions создана")
    public void test04_TEST() {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'sessions'";
            NativeQuery<?> query = session.createNativeQuery(sql);
            BigInteger result = (BigInteger) query.uniqueResult();
            assertEquals(1, result.intValue());
        }

    }*/

}
