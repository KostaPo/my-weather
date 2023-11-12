package util;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.kostapo.myweather.model.Location;
import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.model.User;
import ru.kostapo.myweather.utils.PropertiesUtil;

import java.util.Properties;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        Configuration configuration = getTestConfiguration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Location.class);
        configuration.addAnnotatedClass(Session.class);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        try {
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Exception e) {
            throw new HibernateException("CAN'T BUILD SESSIONFACTORY");
        }
    }

    private static Configuration getTestConfiguration() {
        Configuration configuration = new Configuration();
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", PropertiesUtil.getProperty("test.database.url"));
        properties.setProperty("hibernate.connection.username", PropertiesUtil.getProperty("test.database.username"));
        properties.setProperty("hibernate.connection.password", PropertiesUtil.getProperty("test.database.password"));
        properties.setProperty("hibernate.connection.driver_class", PropertiesUtil.getProperty("test.driver_class"));
        properties.setProperty("hibernate.dialect", PropertiesUtil.getProperty("test.dialect"));
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        configuration.setProperties(properties);
        return configuration;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
