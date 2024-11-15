package salen.tasks.entity.mapper;

import org.mapstruct.Mapper;
import salen.tasks.entity.User;
import salen.tasks.entity.dto.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper extends DefaultMapper<UserDto, User> {
}
