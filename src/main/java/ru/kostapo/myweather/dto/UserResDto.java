package ru.kostapo.myweather.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResDto {
    private String login;
    private String session_id;
}
