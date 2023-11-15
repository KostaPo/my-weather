package ru.kostapo.myweather.servlets.listener;

import ru.kostapo.myweather.model.dao.SessionDAO;
import ru.kostapo.myweather.service.AuthorizationService;
import ru.kostapo.myweather.utils.HibernateUtil;
import ru.kostapo.myweather.utils.PropertiesUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class SchedulerContextListener implements ServletContextListener {

    private ScheduledExecutorService executor;
    private final int poolSize = Integer.parseInt(PropertiesUtil.getProperty("scheduler.pool"));
    private final long init = Long.parseLong(PropertiesUtil.getProperty("scheduler.delay.min"));
    private final long period = Long.parseLong(PropertiesUtil.getProperty("scheduler.period.min"));

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        AuthorizationService authorizationService =
                new AuthorizationService(HibernateUtil.getSessionFactory());
        executor = Executors.newScheduledThreadPool(poolSize);
        Runnable task = () -> {
            try {
                authorizationService.deleteExpiredSessions(LocalDateTime.now());
            } catch (Exception e) {
                throw new RuntimeException("Scheduled Task Exception", e);
            }
        };
        executor.scheduleAtFixedRate(task, init, period, TimeUnit.MINUTES);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (executor != null) {
            executor.shutdownNow();
        }
    }
}
