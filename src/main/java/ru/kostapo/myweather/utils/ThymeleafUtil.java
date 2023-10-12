package ru.kostapo.myweather.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;


public class ThymeleafUtil {

    private static TemplateEngine templateEngine;

    public static TemplateEngine getTemplateEngine(ServletContext servletContext) {
        if(templateEngine == null) {
            ServletContextTemplateResolver templateResolver = getTemplateResolver(servletContext);
            templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);
        }
        return templateEngine;
    }

    private static ServletContextTemplateResolver getTemplateResolver(ServletContext servletContext) {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(0L);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}