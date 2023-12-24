import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import ru.kostapo.myweather.exception.OpenWeatherException;
import ru.kostapo.myweather.model.api.LocationApiRes;
import ru.kostapo.myweather.service.OpenWeatherService;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_03_OpenWeatherService {

    private static CloseableHttpClient httpClient;
    private static CloseableHttpResponse httpResponse;
    private static StatusLine statusLine;
    private static HttpEntity httpEntity;

    @BeforeAll
    public static void init() {
        httpClient = Mockito.mock(CloseableHttpClient.class);
        httpResponse = Mockito.mock(CloseableHttpResponse.class);
        statusLine = Mockito.mock(StatusLine.class);
        httpEntity = Mockito.mock(HttpEntity.class);
    }

    @SneakyThrows
    @Test
    @Order(1)
    @DisplayName("Успешный поиск локаций по названию")
    public void test01_getLocationsByName() {
        String mockResponse = readFileToString("MockLocationResponse");

        Mockito.when(httpClient.execute(Mockito.any(HttpGet.class))).thenReturn(httpResponse);
        Mockito.when(httpResponse.getStatusLine()).thenReturn(statusLine);
        Mockito.when(statusLine.getStatusCode()).thenReturn(200);
        Mockito.when(httpResponse.getEntity()).thenReturn(httpEntity);
        Mockito.when(httpEntity.getContent())
                .thenReturn(new ByteArrayInputStream(mockResponse.getBytes(StandardCharsets.UTF_8)));

        OpenWeatherService openWeatherService = new OpenWeatherService(httpClient);
        List<LocationApiRes> locations = openWeatherService.getLocationsByName("TEST");

        LocationApiRes testLocation = LocationApiRes.builder()
                .name("Moscow")
                .country("RU")
                .state("Moscow")
                .latitude(55.7504461)
                .longitude(37.6174943)
                .build();

        Assertions.assertAll("Проверка локации на наличие, сравнение локаций.",
                () -> assertTrue(locations.contains(testLocation), "Локация не найдена!"),
                () -> assertEquals(testLocation, locations.get(locations.indexOf(testLocation)))
        );
    }

    @SneakyThrows
    @Test
    @Order(2)
    @DisplayName("OpenWeatherException при статусах 4xx")
    public void test02_openWeatherException() {
        Mockito.when(statusLine.getStatusCode()).thenReturn(400);
        OpenWeatherService openWeatherService = new OpenWeatherService(httpClient);

        assertThrows(OpenWeatherException.class,
                () -> openWeatherService.getLocationsByName("TEST"));
    }

    @SneakyThrows
    @Test
    @Order(2)
    @DisplayName("OpenWeatherException при статусах 5xx")
    public void test03_openWeatherException() {
        Mockito.when(statusLine.getStatusCode()).thenReturn(500);
        OpenWeatherService openWeatherService = new OpenWeatherService(httpClient);

        assertThrows(OpenWeatherException.class,
                () -> openWeatherService.getLocationsByName("TEST"));
    }

    private String readFileToString(String filePath) {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.toString();
    }
}
