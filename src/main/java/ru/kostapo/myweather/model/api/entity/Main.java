package ru.kostapo.myweather.model.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {

    @JsonProperty("temp")
    private BigDecimal temperature;

    @JsonProperty("feels_like")
    private BigDecimal feelsLike;

    @JsonProperty("pressure")
    private Integer pressure;

    @JsonProperty("humidity")
    private Integer humidity;

    @JsonProperty("temp_min")
    private BigDecimal tMin;

    @JsonProperty("temp_max")
    private BigDecimal tMax;

    @Override
    public String toString() {
        return "Main{" +
                "Температура=" + temperature +
                ", Давление=" + pressure +
                ", Влажность=" + humidity +
                '}';
    }
}
