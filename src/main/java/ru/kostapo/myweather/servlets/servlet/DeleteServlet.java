package ru.kostapo.myweather.servlets.servlet;

import ru.kostapo.myweather.model.dao.LocationDAO;
import ru.kostapo.myweather.utils.HibernateUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteServlet", value = "/delete")
public class DeleteServlet extends HttpServlet {

    private LocationDAO locationDAO;

    @Override
    public void init() {
        locationDAO = new LocationDAO(HibernateUtil.getSessionFactory());
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long locationId = Long.valueOf(request.getParameter("locationId"));
        locationDAO.deleteById(locationId);
        response.sendRedirect("/");
    }
}
