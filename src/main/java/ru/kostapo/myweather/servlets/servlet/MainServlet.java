package ru.kostapo.myweather.servlets.servlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.kostapo.myweather.exception.UserNotFoundException;
import ru.kostapo.myweather.model.User;
import ru.kostapo.myweather.model.dao.UserDAO;
import ru.kostapo.myweather.model.dto.UserResDto;
import ru.kostapo.myweather.model.mapper.UserMapper;
import ru.kostapo.myweather.utils.HibernateUtil;

import java.io.*;
import java.util.Optional;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "MainServlet", value = "/")
public class MainServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO(HibernateUtil.getSessionFactory());
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, getServletContext());
        String login = (String) request.getSession().getAttribute("user_login");
        try {
            Optional<User> user = userDAO.findByLogin(login);
            if(user.isPresent()) {
                UserResDto userResponse = UserMapper.INSTANCE.toDto(user.get());
                context.setVariable("user", userResponse);
            }
            String output = templateEngine.process("index", context);
            response.getWriter().write(output);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}