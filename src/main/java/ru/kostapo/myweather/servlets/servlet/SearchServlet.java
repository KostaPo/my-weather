package ru.kostapo.myweather.servlets.servlet;

import lombok.extern.log4j.Log4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.kostapo.myweather.exception.BindingResult;
import ru.kostapo.myweather.exception.OpenWeatherException;
import ru.kostapo.myweather.exception.UniqConstraintViolationException;
import ru.kostapo.myweather.model.Location;
import ru.kostapo.myweather.model.User;
import ru.kostapo.myweather.model.api.LocationApiRes;
import ru.kostapo.myweather.model.dao.LocationDAO;
import ru.kostapo.myweather.model.dao.UserDAO;
import ru.kostapo.myweather.model.dto.UserResDto;
import ru.kostapo.myweather.model.mapper.LocationMapper;
import ru.kostapo.myweather.service.OpenWeatherService;
import ru.kostapo.myweather.utils.HibernateUtil;
import ru.kostapo.myweather.utils.HttpClientUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "SearchServlet", value = "/search")
public class SearchServlet extends HttpServlet {

    private LocationDAO locationDAO;
    private UserDAO userDAO;
    private TemplateEngine templateEngine;
    private OpenWeatherService weatherService;

    @Override
    public void init() {
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
        weatherService = new OpenWeatherService(HttpClientUtil.getHttpClient());
        locationDAO = new LocationDAO(HibernateUtil.getSessionFactory());
        userDAO = new UserDAO(HibernateUtil.getSessionFactory());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, getServletContext());

        UserResDto userResponse = (UserResDto) request.getSession().getAttribute("user");
        context.setVariable("user", userResponse);

        List<LocationApiRes> locations = new ArrayList<>();
        if (request.getParameter("location") != null) {
            locations = weatherService.getLocationsByName(request.getParameter("location"));
        }
        context.setVariable("locations", locations);

        String output = templateEngine.process("search", context);
        response.getWriter().write(output);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, getServletContext());

        UserResDto userResponse = (UserResDto) request.getSession().getAttribute("user");
        context.setVariable("user", userResponse);

        LocationApiRes apiResponse = LocationApiRes.builder()
                .name(request.getParameter("name"))
                .latitude(Double.parseDouble(request.getParameter("latitude")))
                .longitude(Double.parseDouble(request.getParameter("longitude")))
                .build();
        Location location = LocationMapper.INSTANCE.toModel(apiResponse);

        Optional<User> usr = userDAO.findByLogin(userResponse.getLogin());
        if (usr.isPresent()) {
            location.setUser(usr.get());
            try {
                locationDAO.save(location);
            } catch (UniqConstraintViolationException ignored) {
            } finally {
                response.sendRedirect(request.getContextPath() + "/");
            }
        }
    }
}
