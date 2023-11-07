package ru.kostapo.myweather.servlets.listener;

import org.hibernate.SessionFactory;
import ru.kostapo.myweather.utils.HibernateUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class HibernateContextListener implements ServletContextListener {

    private SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (sessionFactory != null)
            sessionFactory.close();
    }
}
