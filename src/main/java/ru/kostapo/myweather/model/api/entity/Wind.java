package ru.kostapo.myweather.model.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind {

    @JsonProperty("speed")
    private BigInteger speed;

    @JsonProperty("deg")
    private Integer deg;

    @JsonProperty("gust")
    private BigInteger gust;

    public String getWindSide() {
        if (deg >= 337.5 || deg < 22.5) {
            return "Север";
        } else if (deg >= 22.5 && deg < 67.5) {
            return "Северо-Восток";
        } else if (deg >= 67.5 && deg < 112.5) {
            return "Восток";
        } else if (deg >= 112.5 && deg < 157.5) {
            return "Юго-Восток";
        } else if (deg >= 157.5 && deg < 202.5) {
            return "Юг";
        } else if (deg >= 202.5 && deg < 247.5) {
            return "Юго-Запад";
        } else if (deg >= 247.5 && deg < 292.5) {
            return "Запад";
        } else if (deg >= 292.5 && deg < 337.5) {
            return "Северо-Запад";
        } else {
            return "UNKNOWN";
        }
    }

    @Override
    public String toString() {
        return "Wind{" +
                "Скорость=" + speed + "м/c" +
                ", Направление=" + getWindSide() +
                ", Порыв=" + gust + "м/c" +
                '}';
    }
}
