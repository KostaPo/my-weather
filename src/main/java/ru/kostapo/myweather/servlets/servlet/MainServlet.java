package ru.kostapo.myweather.servlets.servlet;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import ru.kostapo.myweather.model.Location;
import ru.kostapo.myweather.model.api.WeatherApiRes;
import ru.kostapo.myweather.model.dao.LocationDAO;
import ru.kostapo.myweather.model.dto.UserResDto;
import ru.kostapo.myweather.service.OpenWeatherService;
import ru.kostapo.myweather.utils.HibernateUtil;
import ru.kostapo.myweather.utils.HttpClientUtil;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "MainServlet", value = "/")
public class MainServlet extends HttpServlet {

    private OpenWeatherService weatherService;
    private TemplateEngine templateEngine;
    private LocationDAO locationDAO;

    @Override
    public void init() {
        weatherService = new OpenWeatherService(HttpClientUtil.getHttpClient());
        locationDAO = new LocationDAO(HibernateUtil.getSessionFactory());
        templateEngine = (TemplateEngine) getServletContext().getAttribute("templateEngine");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = new WebContext(request, response, getServletContext());

        UserResDto userResponse = (UserResDto) request.getSession().getAttribute("user");
        context.setVariable("user", userResponse);

        List<Location> locations = locationDAO.findByUserId(userResponse.getId()).stream()
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .collect(Collectors.toList());
        Map<Location, WeatherApiRes> weatherList = new LinkedHashMap<>();
        for(Location location : locations) {
            WeatherApiRes tmp = weatherService.getWeatherForLocation(location);
            tmp.setLocationName(location.getName());
            weatherList.put(location,tmp);
        }
        context.setVariable("weatherList", weatherList);

        String output = templateEngine.process("index", context);
        response.getWriter().write(output);
    }
}