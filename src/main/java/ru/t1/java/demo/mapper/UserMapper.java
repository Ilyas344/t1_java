package ru.t1.java.demo.mapper;

import org.mapstruct.*;
import ru.t1.java.demo.model.dto.user.UserDto;
import ru.t1.java.demo.model.user.User;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", ignore = true)
    User updateUserFromDto(UserDto userDto, @MappingTarget User existingUser);


    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "password", source = "password")
    User userDtoMapper(UserDto user);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "username", source = "username")
    UserDto userMapper(User user);

}
