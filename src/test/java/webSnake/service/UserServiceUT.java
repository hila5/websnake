package webSnake.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import webSnake.api.dto.*;
import webSnake.domain.User;
import webSnake.entity.GameEntity;
import webSnake.entity.UserEntity;
import webSnake.exception.ResourceNotFoundException;
import webSnake.exception.UserNameUnavailable;
import webSnake.mapper.GameMapper;
import webSnake.mapper.UserMapper;
import webSnake.repositories.GameRepository;
import webSnake.repositories.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceUT {

    @Mock
    private UserRepository userRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private GameMapper gameMapper;
    @InjectMocks
    private UserService userService;
    private UserEntity userEntity;
    private Long userId = 1L;
    private String username = "hila";
    private String identifier = "1";

    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setUsername(username);
    }

    @Nested
    class CreateUser {

        @Test
        void createUser_success() {
            //Arrange
            CreateUserDto dto = CreateUserDto.builder().username(username).build();
            when(userRepository.findByUsername(username)).thenReturn(null);
            when(userMapper.fromDomain(any(User.class))).thenReturn(userEntity);
            when(userMapper.toResponse(userEntity)).thenReturn(new UserResponseDto());

            //Act
            UserResponseDto response = userService.createUser(dto);

            //Assert
            assertNotNull(response);
            verify(userRepository).save(userEntity);
        }

        @Test
        void createUser_usernameAlreadyExists_shouldThrow() {
            //Arrange
            CreateUserDto dto = CreateUserDto.builder().username(username).build();
            when(userRepository.findByUsername(username)).thenReturn(userEntity);

            //Act
            //Assert
            assertThrows(UserNameUnavailable.class, () -> userService.createUser(dto));
        }

    }

    @Nested
    class GetUserByIdOrUsername {

        @Test
        void getUserByIdOrUsername_getUserById_success() {
            //Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
            when(userMapper.toResponse(userEntity)).thenReturn(new UserResponseDto());

            //Act
            UserResponseDto result = userService.getUserByIdOrUsername(identifier);

            //Assert
            assertNotNull(result);
        }

        @Test
        void GetUserByIdOrUsername_getUserByUsername_success() {
            //Arrange
            when(userRepository.findByUsername(username)).thenReturn(userEntity);
            when(userMapper.toResponse(userEntity)).thenReturn(new UserResponseDto());

            //Act
            UserResponseDto result = userService.getUserByIdOrUsername(username);

            //Assert
            assertNotNull(result);
        }

        @Test
        void getUser_notFound_shouldThrow() {
            //Arrange
            when(userRepository.findByUsername(anyString())).thenReturn(null);

            //Act
            //Assert
            assertThrows(ResourceNotFoundException.class,
                    () -> userService.getUserByIdOrUsername(identifier));
        }

    }

    @Nested
    class GetUserEntityByIdentifier {

        @Test
        void getUserEntityByIdentifier_withId_success() {
            //Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

            //Act
            UserEntity result = userService.getUserEntityByIdentifier(identifier);

            //Assert
            assertEquals(userEntity, result);
        }

        @Test
        void getUserEntityByIdentifier_withUsername_success() {
            //Arrange
            when(userRepository.findByUsername(username)).thenReturn(userEntity);

            //Act
            UserEntity result = userService.getUserEntityByIdentifier(username);

            //Assert
            assertEquals(userEntity, result);
        }

        @Test
        void getUserEntityByIdentifier_notFound_shouldThrow() {
            //Arrange
            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
            when(userRepository.findByUsername(anyString())).thenReturn(null);

            //Act
            //Assert
            assertThrows(ResourceNotFoundException.class,
                    () -> userService.getUserEntityByIdentifier(identifier));
        }

    }

    @Nested
    class DeleteUser {

        @Test
        void deleteUser_success() {
            //Arrange
            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

            //Act
            userService.deleteUser(identifier);

            //Assert
            verify(userRepository).delete(userEntity);
        }

    }

    @Nested
    class GetAllUsers {

        @Test
        void getAllUsers_success() {
            //Arrange
            int usersAmount = 1;
            when(userRepository.findAll()).thenReturn(List.of(userEntity));
            when(userMapper.toResponse(userEntity)).thenReturn(new UserResponseDto());

            //Act
            List<UserResponseDto> result = userService.getAllUsers();

            //Assert
            assertEquals(usersAmount, result.size());
        }

    }

    @Nested
    class GetAllUserGames {

        @Test
        void getAllUserGames_success() {
            //Arrange
            Long userId = 1L;
            String identifier = "1";
            int expectedResultSize = 1;
            GameEntity gameEntity = new GameEntity();
            List<GameEntity> games = List.of(gameEntity);

            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
            when(gameRepository.findByOwnerUserId(userId)).thenReturn(games);
            when(gameMapper.toResponse(gameEntity)).thenReturn(new GameResponseDto());

            //Act
            List<GameResponseDto> result = userService.getAllUserGames(identifier);

            //Assert
            assertEquals(expectedResultSize, result.size());
            verify(gameRepository).findByOwnerUserId(userId);
        }

        @Test
        void getAllUserGames_userNotFound_shouldThrow() {
            //Arrange
            when(userRepository.findByUsername(anyString())).thenReturn(null);

            //Act
            //Assert
            assertThrows(ResourceNotFoundException.class,
                    () -> userService.getAllUserGames(identifier));
        }

    }

    @Nested
    class UpdateLastPlayedForUser {

        @Test
        void updateLastPlayedForUser_success() {
            //Arrange
            //Act
            userService.updateLastPlayedForUser(userEntity);

            //Assert
            assertNotNull(userEntity.getLastPlayed());
            verify(userRepository).save(userEntity);
        }

    }

    @Nested
    class GetScore {

        @Test
        void getScore_withScores() {
            //Arrange
            Long score = 100L;
            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
            when(gameRepository.sumScoresByUserId(userId)).thenReturn(score);

            //Act
            UserSumScoreDto result = userService.getScore(identifier);

            //Assert
            assertEquals(score, result.getSumScore());
        }

        @Test
        void getScore_null_shouldReturnZero() {
            //Arrange
            Long score = 0L;
            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
            when(gameRepository.sumScoresByUserId(userId)).thenReturn(null);

            //Act
            UserSumScoreDto result = userService.getScore(identifier);

            //Assert
            assertEquals(score, result.getSumScore());
        }

    }

    @Nested
    class GetScoreRank {

        @Test
        void getScoreRank_success() {
            //Arrange
            Long rank = 5L;
            int rankInt=5;
            UserScoreRank userScoreRank = mock(UserScoreRank.class);
            when(userScoreRank.getUserRank()).thenReturn(rank);

            when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
            when(gameRepository.findRankForUser(userId)).thenReturn(userScoreRank);

            //Act
            UserRankDto result = userService.getScoreRank(identifier);

            //Assert
            assertEquals(rankInt, result.getRank());
        }

    }

}