package ru.kostapo.myweather.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import ru.kostapo.myweather.model.api.entity.Cloudiness;
import ru.kostapo.myweather.model.api.entity.Main;
import ru.kostapo.myweather.model.api.entity.Weather;
import ru.kostapo.myweather.model.api.entity.Wind;
import ru.kostapo.myweather.utils.SerializeTimeUtil;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherApiRes {

    @JsonProperty("name")
    private String locationName;

    @JsonProperty("dt")
    @JsonDeserialize(using = SerializeTimeUtil.class)
    private String lastUpdate;

    @JsonProperty("weather")
    private List<Weather> weatherList;

    @JsonProperty("main")
    private Main main;

    @JsonProperty("wind")
    private Wind wind;

    @JsonProperty("clouds")
    private Cloudiness clouds;
}
