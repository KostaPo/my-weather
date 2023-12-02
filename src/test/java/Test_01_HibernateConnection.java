import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.*;
import util.TestHibernateUtil;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_01_HibernateConnection {

    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void init() {
        sessionFactory = TestHibernateUtil.getSessionFactory();
    }

    @Test
    @Order(1)
    @DisplayName("SessionFactory не NULL")
    public void test01_SessionFactoryNotNull() {
        assertNotNull(sessionFactory);
    }

    @Test
    @Order(2)
    @DisplayName("Подключение к БД через SessionFactory")
    public void test02_ConnectionToDatabase() {
        try (Session session = sessionFactory.openSession()) {
            assertTrue(session.isConnected());
        }
    }

    @Test
    @Order(3)
    @DisplayName("Таблица users создана")
    public void test03_usersTableExist() {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'users'";
            NativeQuery<?> query = session.createNativeQuery(sql);
            BigInteger result = (BigInteger) query.uniqueResult();
            assertEquals(1, result.intValue());
        }
    }

    @Test
    @Order(4)
    @DisplayName("Таблица sessions создана")
    public void test04_sessionsTableExist() {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'sessions'";
            NativeQuery<?> query = session.createNativeQuery(sql);
            BigInteger result = (BigInteger) query.uniqueResult();
            assertEquals(1, result.intValue());
        }
    }

    @Test
    @Order(5)
    @DisplayName("Таблица locations создана")
    public void test05_locationsTableExist() {
        try (Session session = sessionFactory.openSession()) {
            String sql = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'locations'";
            NativeQuery<?> query = session.createNativeQuery(sql);
            BigInteger result = (BigInteger) query.uniqueResult();
            assertEquals(1, result.intValue());
        }
    }
}
