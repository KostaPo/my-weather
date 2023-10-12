package ru.kostapo.myweather.servlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "MainServlet", value = "/")
public class MainServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init() {
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Создаем контекст и добавляем атрибуты, которые мы хотим использовать в шаблоне
        WebContext webContext = new WebContext(request, response, getServletContext());
        webContext.setVariable("name", "KostaPo");

        // Генерируем HTML из шаблона с использованием TemplateEngine
        String output = templateEngine.process("index", webContext);

        // Отправляем результат клиенту
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(output);
    }
}