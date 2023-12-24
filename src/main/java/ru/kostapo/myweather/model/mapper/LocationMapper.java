package ru.kostapo.myweather.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.kostapo.myweather.model.Location;
import ru.kostapo.myweather.model.api.LocationApiRes;

@Mapper
public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Location toModel(LocationApiRes locationApiRes);
}