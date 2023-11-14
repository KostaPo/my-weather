package ru.kostapo.myweather.servlets.servlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.kostapo.myweather.exception.BindingResult;
import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.model.dto.UserReqDto;
import ru.kostapo.myweather.exception.PasswordMismatchException;
import ru.kostapo.myweather.exception.UserNotFoundException;
import ru.kostapo.myweather.service.AuthorizationService;
import ru.kostapo.myweather.utils.HibernateUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/signin")
public class LoginServlet extends HttpServlet {

    private AuthorizationService authService;
    private TemplateEngine templateEngine;

    @Override
    public void init() {
        authService = new AuthorizationService(HibernateUtil.getSessionFactory());
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext webContext = new WebContext(request, response, getServletContext());
        String output = templateEngine.process("signin", webContext);
        response.getWriter().write(output);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, getServletContext());
        UserReqDto userRequest = UserReqDto.builder()
                .login(request.getParameter("login"))
                .password(request.getParameter("password"))
                .build();
        try {
            Session session = authService.login(userRequest);
            response.addCookie(authService.getNewCookie(session));
            response.sendRedirect("/");
        } catch (UserNotFoundException e) {
            BindingResult bindingResult = new BindingResult("login", e.getMessage());
            context.setVariable("errors", bindingResult);
            templateEngine.process("signin", context, response.getWriter());
        } catch (PasswordMismatchException e) {
            BindingResult bindingResult = new BindingResult("password", e.getMessage());
            context.setVariable("errors", bindingResult);
            templateEngine.process("signin", context, response.getWriter());
        }
    }
}
