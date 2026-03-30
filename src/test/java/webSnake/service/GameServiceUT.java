package webSnake.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webSnake.api.dto.CreateGameDto;
import webSnake.api.dto.GameResponseDto;
import webSnake.domain.Game;
import webSnake.entity.GameEntity;
import webSnake.entity.PointEmbed;
import webSnake.entity.UserEntity;
import webSnake.exception.CurrentGameDeletedException;
import webSnake.exception.GameMappingException;
import webSnake.exception.ResourceNotFoundException;
import webSnake.mapper.GameMapper;
import webSnake.repositories.GameRepository;
import webSnake.repositories.UserRepository;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceUT {

    @Mock
    private GameRepository gameRepositoryMock;
    @Mock
    private GameMapper gameMapperMock;
    @Mock
    private CurrentGameService currentGameServiceMock;
    @Mock
    UserRepository userRepository;

    @Mock
    UserService userService;

    @InjectMocks
    private GameService gameService;
    int rows = 4;
    int cols = 4;
    Long gameId = 100L;
    int score = 2;
    int snakeX = 5;
    int snakeY = 5;
    int appleX = 5;
    int appleY = 5;
    Long userId=1L;
    String username="hila";
    Point snakeLocation = new Point(snakeX, snakeY);
    Point appleLocation = new Point(appleX, appleY);
    String matrixJson = "[[0,0,0,0,0],[0,0,2,0,0],[0,0,0,1,0],[0,0,0,0,0]]";
    private final GameEntity gameEntity = GameEntity.builder().gameId(gameId).rowSize(rows).colSize(cols)
            .snakeLocation(new PointEmbed(snakeLocation)).appleLocation(new PointEmbed(appleLocation)).score(score).build();
    private final UserEntity userEntity = UserEntity.builder().userId(userId).username(username).build();
    private final GameResponseDto gameResponseDto = GameResponseDto.builder().gameId(gameId).board(matrixJson).score(score).build();

    private CreateGameDto requestCreateGameDto() {
        return CreateGameDto.builder().rows(4).cols(4).build();
    }

    @Nested
    class CreateGame {

        @Test
        void createGame_success() {
            //Arrange
            when(userService.getUserEntityByIdentifier(any())).thenReturn(userEntity);
            when(gameMapperMock.fromDomain(ArgumentMatchers.any(Game.class))).thenReturn(gameEntity);
            when(gameRepositoryMock.save(gameEntity)).thenReturn(gameEntity);
            when(currentGameServiceMock.setUserCurrentGame(any(),any())).thenReturn(gameResponseDto);

            //Act
            GameResponseDto responseDto = gameService.createGame(requestCreateGameDto());

            //Assert
            assertEquals(gameResponseDto, responseDto);
        }

        @Test
        void createGame_failure_whileMapping() {
            //Arrange
            when(gameMapperMock.fromDomain(ArgumentMatchers.any(Game.class))).thenThrow(new GameMappingException("cant map"));

            //Act
            //Assert
            assertThrows(GameMappingException.class, () -> gameService.createGame(requestCreateGameDto()));
        }

    }

    @Nested
    class GetAllGames {

        @Test
        void getAllGames_success() {
            //Arrange
            List<GameResponseDto> expectedResult = List.of(gameResponseDto);
            when(gameRepositoryMock.findAll()).thenReturn(Arrays.asList(gameEntity));
            when(gameMapperMock.toResponse(gameEntity)).thenReturn(gameResponseDto);

            //Act
            List<GameResponseDto> responseDtos = gameService.getAllGames();

            //Assert
            assertEquals(expectedResult, responseDtos);
        }

    }

    @Nested
    class DeleteGame {

        @Test
        void deleteGame_success() {
            //Arrange
            when(gameRepositoryMock.existsById(gameId)).thenReturn(true);
            when(userRepository.findByCurrentGameId(gameId)).thenReturn(null);
            doNothing().when(gameRepositoryMock).deleteById(gameId);

            //Act
            gameService.deleteGame(gameId);

            //Assert
            verify(gameRepositoryMock).deleteById(gameId);
            verify(gameRepositoryMock).existsById(gameId);
        }

        @Test
        void deleteGame_failure_GameIsUserCurrentGame() {
            //Arrange
            when(gameRepositoryMock.existsById(gameId)).thenReturn(true);
            when(userRepository.findByCurrentGameId(gameId)).thenReturn(userEntity);

            //Act
            //Assert
            assertThrows(CurrentGameDeletedException.class,()->gameService.deleteGame(gameId));
            verify(gameRepositoryMock).existsById(gameId);
        }

        @Test
        void deleteGame_failure_NotFoundGame() {
            //Arrange
            when(gameRepositoryMock.existsById(gameId)).thenReturn(false);

            //Act
            //Assert
            assertThrows(ResourceNotFoundException.class, () -> gameService.deleteGame(gameId));
        }

    }

}