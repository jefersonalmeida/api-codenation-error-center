package com.error.center.mapper;

import com.error.center.dto.UserDTO;
import com.error.center.entity.User;
import com.error.center.util.BCrypt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(source = "password", target = "password", ignore = true),
    })
    UserDTO toDTO(User dto);

    @Mappings({
            @Mapping(source = "password", target = "password", qualifiedByName = "GenerateHash"),
    })
    User toEntity(UserDTO entity);

    List<UserDTO> map(List<User> entities);

    @Named("GenerateHash")
    default String generateHash(String input) {
        return BCrypt.getHash(input);
    }
}
