package ru.kostapo.myweather.servlets.servlet.auth;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.kostapo.myweather.model.dto.UserReqDto;
import ru.kostapo.myweather.exception.BindingResult;
import ru.kostapo.myweather.model.dto.UserResDto;
import ru.kostapo.myweather.exception.UniqConstraintViolationException;
import ru.kostapo.myweather.exception.ValidConstraintViolationException;
import ru.kostapo.myweather.service.UserServiceImpl;
import ru.kostapo.myweather.utils.PropertiesUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "RegistrationServlet", value = "/signup")
public class RegistrationServlet extends HttpServlet {

    private TemplateEngine templateEngine;
    private UserServiceImpl userServiceImpl;

    @Override
    public void init() {
        userServiceImpl = new UserServiceImpl();
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext webContext = new WebContext(request, response, getServletContext());
        String output = templateEngine.process("signup", webContext);
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
            UserResDto userResponse = userServiceImpl.userRegistration(userRequest);
            Cookie cookie = new Cookie("session_id", userResponse.getSession_id());
            long ttlMin = Long.parseLong(PropertiesUtil.getProperty("session_ttl"));
            cookie.setMaxAge((int) (ttlMin * 60));
            response.addCookie(cookie);
            request.getSession().setAttribute("user", userResponse);
            response.sendRedirect("/");
        } catch (ValidConstraintViolationException e) {
            BindingResult bindingResult = new BindingResult(e.getConstraintViolations());
            context.setVariable("errors", bindingResult);
            templateEngine.process("signup", context, response.getWriter());
        } catch (UniqConstraintViolationException e) {
            BindingResult bindingResult =
                    new BindingResult("login",
                            String.format("Логин %s занят, введите другой!", request.getParameter("login")));
            context.setVariable("errors", bindingResult);
            templateEngine.process("signup", context, response.getWriter());
        }
    }
}
