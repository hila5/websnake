package webSnake.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webSnake.api.dto.GameResponseDto;
import webSnake.domain.Game;
import webSnake.domain.MoveSnakeEnum;
import webSnake.entity.GameEntity;
import webSnake.entity.PointEmbed;
import webSnake.entity.UserEntity;
import webSnake.exception.GameMappingException;
import webSnake.exception.NotYoursGameException;
import webSnake.exception.ResourceNotFoundException;
import webSnake.exception.SnakeOutOfBoundsException;
import webSnake.mapper.GameMapper;
import webSnake.repositories.GameRepository;
import webSnake.repositories.UserRepository;

import java.awt.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrentGameServiceUT {

    @Mock
    private GameRepository gameRepositoryMock;
    @Mock
    private UserRepository userRepositoryMock;
    @Mock
    private UserService userServiceMock;
    @Mock
    private GameMapper gameMapperMock;
    @InjectMocks
    private CurrentGameService currentGameService;

    Long gameId = 100L;
    String board = "[[1,0,0,0],[0,2,0,0],[0,0,0,0],[0,0,0,0]]";
    int rowSize = 4;
    int colSize = 4;
    int snakeX = 0;
    int snakeY = 0;
    Point snakePoint = new Point(snakeX, snakeY);
    PointEmbed snakePointEmb = new PointEmbed(snakePoint);
    int appleX = 1;
    int appleY = 1;
    Point applePoint = new Point(appleX, appleY);
    PointEmbed applePointEmb = new PointEmbed(applePoint);
    int score = 10;
    Game game = new Game(rowSize, colSize, snakePoint, applePoint, score);
    Long userId = 1L;
    String userIdentifier = "hila";
    UserEntity userEntity = UserEntity.builder().userId(userId).build();
    GameEntity gameEntity = GameEntity.builder().snakeLocation(snakePointEmb).appleLocation(applePointEmb).gameId(gameId).score(score).colSize(colSize).rowSize(rowSize).owner(userEntity).build();
    GameResponseDto gameResponseDto = GameResponseDto.builder().gameId(gameId).board(board).score(score).build();

    @Nested
    class setUserCurrentGame {

        @Test
        void setUserCurrentGame_success() {
            //Arrange
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.ofNullable(gameEntity));
            when(userRepositoryMock.save(userEntity)).thenReturn(userEntity);
            when(gameMapperMock.toResponse(gameEntity)).thenReturn(gameResponseDto);

            //Act
            GameResponseDto result = currentGameService.setUserCurrentGame(userIdentifier, gameId);

            //Assert
            assertEquals(gameResponseDto, result);

        }

        @Test
        void setUserCurrentGame_gameNotFound_throwsResourceNotFoundException() {
            // Arrange
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.empty());

            // Act // Assert
            assertThrows(ResourceNotFoundException.class,
                    () -> currentGameService.setUserCurrentGame(userIdentifier, gameId));
        }

        @Test
        void setUserCurrentGame_notOwner_throwsNotYoursGameException() {
            // Arrange
            UserEntity otherUser = UserEntity.builder().userId(99L).build();
            GameEntity otherGame = GameEntity.builder().gameId(gameId).owner(otherUser).build();

            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(otherGame));

            // Act //Assert
            assertThrows(NotYoursGameException.class,
                    () -> currentGameService.setUserCurrentGame(userIdentifier, gameId));
        }

    }

    @Nested
    class getUserCurrentGameEntity {

        @Test
        void getUserCurrentGameEntity_success() {
            // Arrange
            userEntity.setCurrentGameId(gameId);
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(gameEntity));

            // Act
            GameEntity result = currentGameService.getUserCurrentGameEntity(userIdentifier);

            // Assert
            assertEquals(gameEntity, result);
        }

        @Test
        void getCurrentGameEntity_fail_whenUserNotFound() {
            //Arrange
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenThrow(new ResourceNotFoundException("Resource not found"));

            //Act
            //Assert
            assertThrows(ResourceNotFoundException.class, () -> currentGameService.getUserCurrentGameEntity(userIdentifier));
        }

        @Test
        void getCurrentGameEntity_fail_whenGameNotFound() {
            //Arrange
            userEntity.setCurrentGameId(gameId);
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenThrow(new ResourceNotFoundException("Resource not found"));

            //Act
            //Assert
            assertThrows(ResourceNotFoundException.class, () -> currentGameService.getUserCurrentGameEntity(userIdentifier));
        }

        @Test
        void getCurrentGameEntity_fail_whenCurrentGameNotSet() {
            //Arrange
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);

            //Act
            //Assert
            assertThrows(ResourceNotFoundException.class, () -> currentGameService.getUserCurrentGameEntity(userIdentifier));
        }

        @Test
        void getUserCurrentGameEntity_fail_whenCantFindInRepo() {
            // Arrange
            userEntity.setCurrentGameId(gameId);
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.empty());

            // Act
            // Assert
            assertThrows(ResourceNotFoundException.class, () -> currentGameService.getUserCurrentGameEntity(userIdentifier));
        }

    }

    @Nested
    class getUserCurrentGame {

        @Test
        void getUserCurrentGame_success() {
            //Arrange
            userEntity.setCurrentGameId(gameId);
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(gameEntity));
            when(gameMapperMock.toResponse(gameEntity)).thenReturn(gameResponseDto);

            //Act
            GameResponseDto resultDto = currentGameService.getUserCurrentGame(userIdentifier);

            //Assert
            assertEquals(gameResponseDto, resultDto);
        }

        @Test
        void getUserCurrentGame_fail_whenUserNotFound() {
            //Arrange
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenThrow(new ResourceNotFoundException("Resource not found"));

            //Act
            //Assert
            assertThrows(ResourceNotFoundException.class, () -> currentGameService.getUserCurrentGame(userIdentifier));

        }

        @Test
        void getUserCurrentGame_fail_whenCurrentGameNotFound() {
            //Arrange
            userEntity.setCurrentGameId(gameId);
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenThrow(new ResourceNotFoundException("Resource not found"));

            //Act
            //Assert
            assertThrows(ResourceNotFoundException.class, () -> currentGameService.getUserCurrentGame(userIdentifier));
        }

        @Test
        void getUserCurrentGame_fail_whenCurrentGameNotSet() {
            //Arrange
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);

            //Act
            //Assert
            assertThrows(ResourceNotFoundException.class, () -> currentGameService.getUserCurrentGame(userIdentifier));
        }

    }

    @Nested
    class MoveSnake {

        @Test
        void moveSnake_success() throws Exception {
            //Arrange
            MoveSnakeEnum moveSnakeEnum = mock(MoveSnakeEnum.class);
            userEntity.setCurrentGameId(gameId);
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(gameEntity));
            when(gameMapperMock.toGameDomain(gameEntity)).thenReturn(game);
            doNothing().when(moveSnakeEnum).move(game);

            //updateGameInDB
            when(gameMapperMock.fromDomain(game)).thenReturn(gameEntity);
            when(gameRepositoryMock.save(gameEntity)).thenReturn(gameEntity);
            when(gameMapperMock.toResponse(gameEntity)).thenReturn(gameResponseDto);
            doNothing().when(userServiceMock).updateLastPlayedForUser(userEntity);
            //Act
            GameResponseDto responseDto = currentGameService.moveSnake(userIdentifier, moveSnakeEnum);

            //Assert
            assertEquals(gameResponseDto, responseDto);
        }

        @Test
        void moveSnake_failed_cantMapToGameDomain() throws Exception {
            //Arrange
            MoveSnakeEnum moveSnakeEnum = mock(MoveSnakeEnum.class);
            userEntity.setCurrentGameId(gameId);
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(gameEntity));
            when(gameMapperMock.toGameDomain(gameEntity)).thenThrow(new GameMappingException("mapping error"));

            //Act//Assert
            assertThrows(GameMappingException.class, () -> currentGameService.moveSnake(userIdentifier, moveSnakeEnum));
        }

        @Test
        void moveSnake_fail_cantMoveSnake() throws Exception {
            //Arrange
            MoveSnakeEnum moveSnakeEnum = mock(MoveSnakeEnum.class);
            userEntity.setCurrentGameId(gameId);
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(gameEntity));
            when(gameMapperMock.toGameDomain(gameEntity)).thenReturn(game);
            doThrow(new SnakeOutOfBoundsException("Snake out of bounds")).when(moveSnakeEnum).move(game);

            //Act
            //Assert
            assertThrows(SnakeOutOfBoundsException.class, () -> currentGameService.moveSnake(userIdentifier, moveSnakeEnum));
        }

        @Test
        void moveSnake_fail_cantMapFromDomain() throws Exception {
            //Arrange
            MoveSnakeEnum moveSnakeEnum = mock(MoveSnakeEnum.class);
            userEntity.setCurrentGameId(gameId);
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(gameEntity));
            when(gameMapperMock.toGameDomain(gameEntity)).thenReturn(game);
            doNothing().when(moveSnakeEnum).move(game);

            //updateGameInDB
            when(gameMapperMock.fromDomain(game)).thenThrow(new GameMappingException("cant map"));

            //Act
            //Assert
            assertThrows(GameMappingException.class, () -> currentGameService.moveSnake(userIdentifier, moveSnakeEnum));
        }

        @Test
        void moveSnake_fail_cantMap() throws Exception {
            //Arrange
            //Arrange
            MoveSnakeEnum moveSnakeEnum = mock(MoveSnakeEnum.class);
            userEntity.setCurrentGameId(gameId);
            when(userServiceMock.getUserEntityByIdentifier(userIdentifier)).thenReturn(userEntity);
            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.of(gameEntity));
            when(gameMapperMock.toGameDomain(gameEntity)).thenReturn(game);
            doNothing().when(moveSnakeEnum).move(game);

            //updateGameInDB
            when(gameMapperMock.fromDomain(game)).thenReturn(gameEntity);
            when(gameRepositoryMock.save(gameEntity)).thenReturn(gameEntity);
            when(gameMapperMock.toResponse(gameEntity)).thenReturn(gameResponseDto);
            doNothing().when(userServiceMock).updateLastPlayedForUser(userEntity);

            when(gameMapperMock.toResponse(gameEntity)).thenThrow(new GameMappingException("Game mapping exception"));

            //Act
            //Assert
            assertThrows(GameMappingException.class, () -> currentGameService.moveSnake(userIdentifier, moveSnakeEnum));
        }

    }

}