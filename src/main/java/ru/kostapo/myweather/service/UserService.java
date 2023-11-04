package ru.kostapo.myweather.service;

import ru.kostapo.myweather.dto.UserReqDto;
import ru.kostapo.myweather.dto.UserResDto;

public interface UserService {
    UserResDto userRegistration (UserReqDto userReqDto);
    UserResDto userLogin(UserReqDto userReqDto);
}
