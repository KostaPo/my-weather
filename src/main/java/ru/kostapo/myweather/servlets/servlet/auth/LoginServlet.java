package ru.kostapo.myweather.servlets.servlet.auth;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.kostapo.myweather.exception.BindingResult;
import ru.kostapo.myweather.model.dto.UserReqDto;
import ru.kostapo.myweather.model.dto.UserResDto;
import ru.kostapo.myweather.exception.PasswordMismatchException;
import ru.kostapo.myweather.exception.UserNotFoundException;
import ru.kostapo.myweather.service.UserServiceImpl;
import ru.kostapo.myweather.utils.PropertiesUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/signin")
public class LoginServlet extends HttpServlet {

    private UserServiceImpl userServiceImpl;
    private TemplateEngine templateEngine;

    @Override
    public void init() {
        userServiceImpl = new UserServiceImpl();
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
            UserResDto userResponse = userServiceImpl.userLogin(userRequest);
            Cookie cookie = new Cookie("session_id", userResponse.getSession_id());
            long ttlHours = Long.parseLong(PropertiesUtil.getProperty("session_ttl"));
            cookie.setMaxAge((int) (ttlHours * 60 * 60));
            response.addCookie(cookie);
            request.getSession().setAttribute("user", userResponse);
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
