package ru.kostapo.myweather.model.service;

import ru.kostapo.myweather.model.dto.UserReqDto;
import ru.kostapo.myweather.model.dto.UserResDto;


public interface UserService {
    UserResDto userRegistration (UserReqDto userReqDto);
    UserResDto userLogin(UserReqDto userReqDto);
    UserResDto findByKey(String key);
}
