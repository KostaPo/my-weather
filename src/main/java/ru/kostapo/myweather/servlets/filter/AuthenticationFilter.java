package ru.kostapo.myweather.servlets.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter(urlPatterns = "/*")
public class AuthenticationFilter implements Filter {

    private List<String> allowedUrlsForNoAuthenticatedUsers;

    @Override
    public void init(FilterConfig filterConfig) {
        allowedUrlsForNoAuthenticatedUsers = Arrays.asList("/signin", "/signup");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestUrl = request.getRequestURI().substring(request.getContextPath().length());

        if (requestUrl.startsWith(request.getContextPath() + "/static/")) {
            filterChain.doFilter(request, response);
        } else {
            String session_id = getSessionFromCookies(request);
            if (session_id != null) {
                if (allowedUrlsForNoAuthenticatedUsers.contains(requestUrl)) {
                    response.sendRedirect("/");
                    return;
                }
            } else {
                if (!allowedUrlsForNoAuthenticatedUsers.contains(requestUrl)) {
                    response.sendRedirect("/signin");
                    return;
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
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
