package ru.kostapo.myweather.listener;

import org.hibernate.SessionFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import ru.kostapo.myweather.utils.HibernateUtil;
import ru.kostapo.myweather.utils.ThymeleafUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextListener implements ServletContextListener {
    private SessionFactory sessionFactory;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        ServletContext context = servletContextEvent.getServletContext();
        context.setRequestCharacterEncoding("UTF-8");
        context.setResponseCharacterEncoding("UTF-8");

        TemplateEngine templateEngine =
                ThymeleafUtil.getTemplateEngine(context);
        context.setAttribute("templateEngine", templateEngine);

        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (sessionFactory != null)
            sessionFactory.close();
    }
}
