package ru.kostapo.myweather.servlets.listener;

import org.thymeleaf.TemplateEngine;
import ru.kostapo.myweather.utils.ThymeleafUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ThymeleafContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext context = servletContextEvent.getServletContext();
        context.setRequestCharacterEncoding("UTF-8");
        context.setResponseCharacterEncoding("UTF-8");
        TemplateEngine templateEngine = ThymeleafUtil.getTemplateEngine(context);
        context.setAttribute("templateEngine", templateEngine);
    }
}
