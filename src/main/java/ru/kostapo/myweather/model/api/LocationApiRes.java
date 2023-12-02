package ru.kostapo.myweather.model.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationApiRes {

    @JsonProperty("name")
    private String name;

    @JsonProperty("country")
    private String country;

    @JsonProperty("lat")
    private BigDecimal latitude;

    @JsonProperty("lon")
    private BigDecimal longitude;

    @Override
    public String toString() {
        return "LocationApiRes{" +
                "name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", lat=" + latitude +
                ", lon=" + longitude +
                '}';
    }
}
