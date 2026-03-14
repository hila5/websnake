package webSnake.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import webSnake.api.dto.UserResponseDto;
import webSnake.domain.User;
import webSnake.entity.UserEntity;

@Mapper(componentModel = "spring", uses = {GameMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponseDto toResponse(UserEntity userEntity);

    UserEntity fromDomain(User user);

}