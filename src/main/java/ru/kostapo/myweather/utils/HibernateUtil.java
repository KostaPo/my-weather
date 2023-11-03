package ru.kostapo.myweather.utils;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.kostapo.myweather.model.Location;
import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.model.User;

import java.util.Properties;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", PropertiesUtil.getProperty("database.url"));
        properties.setProperty("hibernate.connection.username", PropertiesUtil.getProperty("database.username"));
        properties.setProperty("hibernate.connection.password", PropertiesUtil.getProperty("database.password"));
        properties.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");

        Configuration configuration = new Configuration();
        configuration.setProperties(properties);
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

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
