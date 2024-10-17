package ru.t1.java.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.t1.java.demo.model.dto.auth.JwtResponse;
import ru.t1.java.demo.model.user.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface JwtMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    JwtResponse toDto(User user);


}