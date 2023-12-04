package ru.kostapo.myweather.servlets.filter;

import ru.kostapo.myweather.model.Session;
import ru.kostapo.myweather.model.dao.SessionDAO;
import ru.kostapo.myweather.servlets.servlet.service.AuthorizationService;
import ru.kostapo.myweather.utils.HibernateUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebFilter(urlPatterns = "/*")
public class SessionCheckFilter implements Filter {

    private SessionDAO sessionDAO;

    private AuthorizationService authService;

    @Override
    public void init(FilterConfig filterConfig)  {
        sessionDAO = new SessionDAO(HibernateUtil.getSessionFactory());
        authService = new AuthorizationService(HibernateUtil.getSessionFactory());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String session_id = getSessionFromCookies(request);

        if (session_id != null) {
            Optional<Session> session = sessionDAO.findById(session_id);
            if(session.isPresent()) {
                request.getSession().setAttribute("user_login", session.get().getUser().getLogin());
            } else {
                authService.logout(request, response);
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
