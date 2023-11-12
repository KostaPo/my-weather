package ru.kostapo.myweather.servlets.servlet.auth;

import ru.kostapo.myweather.model.service.SessionService;
import ru.kostapo.myweather.model.service.SessionServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet (name = "Logout", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

    SessionService sessionService = new SessionServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie cleaningCookies = new Cookie("session_id", null);
        cleaningCookies.setMaxAge(0);
        response.addCookie(cleaningCookies);
        request.getSession().invalidate();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_id")) {
                    sessionService.deleteById(cookie.getValue());
                }
            }
        }
        response.sendRedirect("/signin");
    }
}
