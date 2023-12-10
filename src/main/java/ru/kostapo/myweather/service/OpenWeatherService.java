package ru.kostapo.myweather.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import ru.kostapo.myweather.exception.OpenWeatherException;
import ru.kostapo.myweather.model.Location;
import ru.kostapo.myweather.model.api.LocationApiRes;
import ru.kostapo.myweather.model.api.WeatherApiRes;

import java.net.URI;
import java.util.List;

public class OpenWeatherService {

    private final ObjectMapper objectMapper;
    private final CloseableHttpClient httpClient;

    private static final String APP_ID = System.getenv("API_KEY");
    private static final String BASE_API_URL = "https://api.openweathermap.org";
    private static final String GEOCODING_API_URL_SUFFIX = "/geo/1.0/direct";
    private static final String WEATHER_API_URL_SUFFIX = "/data/2.5/weather";

    public OpenWeatherService(CloseableHttpClient client) {
        objectMapper = new ObjectMapper();
        httpClient = client;
    }

    public List<LocationApiRes> getLocationsByName(String name) {
        HttpGet httpGet = new HttpGet(getUriForGeocodingRequest(name));
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 400) {
                throw new OpenWeatherException("OpenWeatherApiException");
            }
            String responseBody = EntityUtils.toString(response.getEntity());
            return objectMapper.readValue(responseBody, new TypeReference<List<LocationApiRes>>() {
            });
        } catch (OpenWeatherException e) {
            throw new OpenWeatherException("OpenWeatherApiException");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка Сети", e);
        }
    }

    public WeatherApiRes getWeatherForLocation(Location location) {
        HttpGet httpGet = new HttpGet(getUriForWeatherRequest(location));
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 400) {
                throw new OpenWeatherException("OpenWeatherApiException");
            }
            String responseBody = EntityUtils.toString(response.getEntity());
            return objectMapper.readValue(responseBody, WeatherApiRes.class);
        } catch (OpenWeatherException e) {
            throw new OpenWeatherException("OpenWeatherApiException");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка Сети", e);
        }
    }

    private URI getUriForGeocodingRequest(String locationName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BASE_API_URL)
                .append(GEOCODING_API_URL_SUFFIX)
                .append("?q=")
                .append(locationName)
                .append("&limit=5")
                .append("&appid=")
                .append(APP_ID);
        return URI.create(stringBuilder.toString());
    }

    private URI getUriForWeatherRequest(Location location) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(BASE_API_URL)
                .append(WEATHER_API_URL_SUFFIX)
                .append("?lat=")
                .append(location.getLatitude())
                .append("&lon=")
                .append(location.getLongitude())
                .append("&appid=")
                .append(APP_ID)
                .append("&units=metric");
        System.out.println(stringBuilder);
        return URI.create(stringBuilder.toString());
    }
}
