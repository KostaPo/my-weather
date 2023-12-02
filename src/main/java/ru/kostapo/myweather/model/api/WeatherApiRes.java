package ru.kostapo.myweather.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import ru.kostapo.myweather.model.api.entity.Cloudiness;
import ru.kostapo.myweather.model.api.entity.Main;
import ru.kostapo.myweather.model.api.entity.Weather;
import ru.kostapo.myweather.model.api.entity.Wind;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiRes {

    @JsonProperty("name")
    private String locationName;

    @JsonProperty("dt")
    private long requestTime;

    @JsonProperty("timezone")
    private long timezone;

    @JsonProperty("weather")
    private List<Weather> weatherList;

    @JsonProperty("main")
    private Main main;

    @JsonProperty("wind")
    private Wind wind;

    @JsonProperty("clouds")
    private Cloudiness clouds;

    @Override
    public String toString() {
        return "WeatherApiRes{" + "\n" +
                "Локация='" + locationName + "\n" +
                ", Погода=" + weatherList + "\n" +
                ", Общее=" + main + "\n" +
                ", Ветер=" + wind + "\n" +
                ", Облака=" + clouds + "\n" +
                '}' + "\n";
    }
}
