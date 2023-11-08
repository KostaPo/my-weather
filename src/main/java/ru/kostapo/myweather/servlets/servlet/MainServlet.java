package ru.kostapo.myweather.servlets.servlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.kostapo.myweather.model.dto.UserResDto;

import java.io.*;
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
        WebContext webContext = new WebContext(request, response, getServletContext());
        UserResDto user = (UserResDto) request.getSession().getAttribute("user");
        webContext.setVariable("user", user);
        String output = templateEngine.process("index", webContext);
        response.getWriter().write(output);
    }
}