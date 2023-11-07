package ru.kostapo.myweather.service;

import ru.kostapo.myweather.model.dto.UserReqDto;
import ru.kostapo.myweather.model.dto.UserResDto;

public interface UserService {
    UserResDto userRegistration (UserReqDto userReqDto);
    UserResDto userLogin(UserReqDto userReqDto);
}
