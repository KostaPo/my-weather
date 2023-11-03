package ru.kostapo.myweather.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserReqDto {
    private String login;
    private String password;
}
