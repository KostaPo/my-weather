package ru.kostapo.myweather.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.kostapo.myweather.dto.UserReqDto;
import ru.kostapo.myweather.dto.UserResDto;
import ru.kostapo.myweather.model.User;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    User toModel(UserReqDto userDto);

    @Mapping(expression = "java(user.getSessions().isEmpty() ? null : user.getSessions().stream().findFirst().get().getId())", target = "session_id")
    UserResDto toDto(User user);
}