package webSnake.mapper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import webSnake.api.dto.UserResponseDto;
import webSnake.domain.User;
import webSnake.entity.UserEntity;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperUT {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    String username = "hila";
    Long userId = 1L;
    UserEntity userEntity = UserEntity.builder().userId(userId).username(username).build();
    User user = new User(username);

    @Nested
    class ToResponse {

        @Test
        void toResponse_success() {
            // Act
            UserResponseDto result = mapper.toResponse(userEntity);

            // Assert
            assertNotNull(result);
            assertEquals(userId, result.getUserId());
            assertEquals(username, result.getUsername());
        }

        @Test
        void toResponse_nullInput_returnsNull() {
            // Act
            UserResponseDto result = mapper.toResponse(null);

            // Assert
            assertNull(result);
        }

    }

    @Nested
    class FromDomain {

        @Test
        void fromDomain_success() {
            // Act
            UserEntity result = mapper.fromDomain(user);

            // Assert
            assertNotNull(result);
            assertEquals(username, result.getUsername());
        }

        @Test
        void fromDomain_nullInput_returnsNull() {
            // Act
            UserEntity result = mapper.fromDomain(null);

            // Assert
            assertNull(result);
        }

    }

}