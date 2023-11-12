package ru.kostapo.myweather.servlets.filter;

import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.model.service.SessionService;
import ru.kostapo.myweather.model.service.SessionServiceImpl;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebFilter(urlPatterns = "/*")
public class SessionCheckFilter implements Filter {

    private SessionService sessionService;


    @Override
    public void init(FilterConfig filterConfig)  {
        this.sessionService = new SessionServiceImpl();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String session_id = getSessionFromCookies(request);

        if (session_id != null) {
            Optional<Session> session = sessionService.findById(session_id);
            if(session.isPresent()) {
                request.getSession().setAttribute("user_login", session.get().getUser().getLogin());
            } else {
                Cookie cleaningCookies = new Cookie("session_id", null);
                cleaningCookies.setMaxAge(0);
                response.addCookie(cleaningCookies);
                request.getSession().invalidate();
                response.sendRedirect("/signin");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }

    private String getSessionFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_id")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
