package com.peter.shortener.features.user.mapper;

import com.peter.shortener.features.user.dto.response.UserDetailedResponse;
import com.peter.shortener.features.user.dto.response.UserResponse;
import com.peter.shortener.features.user.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toDto(User user);

    UserDetailedResponse toDetailedDto(User user);

}
