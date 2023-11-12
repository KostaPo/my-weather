package ru.kostapo.myweather.servlets.servlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.kostapo.myweather.model.dto.UserResDto;
import ru.kostapo.myweather.model.service.UserService;
import ru.kostapo.myweather.model.service.UserServiceImpl;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "MainServlet", value = "/")
public class MainServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private UserService userService;

    @Override
    public void init() {
        userService = new UserServiceImpl();
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext webContext = new WebContext(request, response, getServletContext());
        String login = (String) request.getSession().getAttribute("user_login");
        UserResDto userResponse = userService.findByKey(login);
        webContext.setVariable("user", userResponse);
        String output = templateEngine.process("index", webContext);
        response.getWriter().write(output);
    }
}