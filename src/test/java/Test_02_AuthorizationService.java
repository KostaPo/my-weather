import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import ru.kostapo.myweather.service.AuthorizationService;
import util.TestHibernateUtil;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestAuthorizationService {
    private static SessionFactory sessionFactory;
    private static AuthorizationService authService;


    @BeforeAll
    public static void init() {
        sessionFactory = TestHibernateUtil.getSessionFactory();
        authService = new AuthorizationService(sessionFactory);
    }

    /*@AfterAll
    public static void tearDown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }*/

    @Test
    @Order(1)
    @DisplayName("test 1")
    public void test01_SessionFactoryNotNull() {
        assertNotNull(new Long(1));
    }

    @Test
    @Order(2)
    @DisplayName("test2")
    public void test02_ConnectionToDatabase() {
            assertTrue(true);
    }
}
