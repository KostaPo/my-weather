package ru.kostapo.myweather.servlets.servlet;

import ru.kostapo.myweather.servlets.servlet.service.AuthorizationService;
import ru.kostapo.myweather.utils.HibernateUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet (name = "Logout", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    private AuthorizationService authService;

    @Override
    public void init() {
        authService = new AuthorizationService(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.logout(request, response);
        response.sendRedirect("/signin");
    }
}
