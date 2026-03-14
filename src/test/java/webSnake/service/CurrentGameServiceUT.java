//package webSnake.service;
//
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import webSnake.api.dto.GameResponseDto;
//import webSnake.domain.Game;
//import webSnake.domain.MoveSnakeEnum;
//import webSnake.entity.GameEntity;
//import webSnake.exception.GameMappingException;
//import webSnake.exception.ResourceNotFoundException;
//import webSnake.exception.SnakeOutOfBoundsException;
//import webSnake.mapper.GameMapper;
//import webSnake.repositories.GameRepository;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class CurrentGameServiceUT {
//
//    @Mock
//    private GameRepository gameRepositoryMock;
//    @Mock
//    private GameMapper gameMapperMock;
//
//    @InjectMocks
//    private CurrentGameService currentGameService;
//    Long firstGameId = 1L;
//    Long gameId = 100L;
//    int score = 2;
//    int snakeX = 5;
//    int snakeY = 5;
//    int appleX = 5;
//    int appleY = 5;
//    String matrixJson = "[[0,0,0,0,0],[0,0,2,0,0],[0,0,0,1,0],[0,0,0,0,0]]";
//    int[][] matrix = {{0, 0, 0, 0}, {0, 0, 2, 0, 0}, {0, 0, 1, 0}, {0, 0, 0, 0}};
//    private Game game = new Game(matrix, snakeX, snakeY, appleX, appleY, score);
//    private GameResponseDto gameResponseDto = GameResponseDto.builder().gameId(gameId).board(matrixJson).score(score).build();
//    private GameEntity gameEntity = GameEntity.builder().gameId(gameId).matrixJson(matrixJson).score(score).snakeX(snakeX).snakeY(snakeY).appleX(appleX).appleY(appleY).build();
//    private CurrentGameEntity currentGameEntity = CurrentGameEntity.builder().gameId(gameId).id(firstGameId).build();
//
//    @Nested
//    class SetCurrentGame {
//
//        @Test
//        void setCurrentGame_success() {
//            //Arrange
//            when(gameRepositoryMock.findById(gameId)).thenReturn(Optional.ofNullable(gameEntity));
//            when(currentGameRepositoryMock.findById(firstGameId)).thenReturn(Optional.ofNullable(currentGameEntity));
//            when(currentGameRepositoryMock.save(currentGameEntity)).thenReturn(currentGameEntity);
//            when(gameMapperMock.toResponse(gameEntity)).thenReturn(gameResponseDto);
//
//            //Act
//            GameResponseDto resultDto = currentGameService.setCurrentGame(gameId);
//
//            //Assert
//            assertEquals(gameResponseDto, resultDto);
//            verify(gameRepositoryMock).findById(gameId);
//            verify(currentGameRepositoryMock).save(currentGameEntity);
//            verify(gameMapperMock).toResponse(gameEntity);
//        }
//
//        @Test
//        void setCurrentGame_fail() {
//            //Arrange
//            when(gameRepositoryMock.findById(gameId)).thenThrow(new ResourceNotFoundException("Resource not found"));
//
//            //Act//Assert
//            assertThrows(ResourceNotFoundException.class, () -> currentGameService.setCurrentGame(gameId));
//        }
//
//    }
//
//    @Nested
//    class GetCurrentGameEntity {
//
//        @Test
//        void getCurrentGameEntity_success() {
//            //Arrange
//            when(currentGameRepositoryMock.findById(firstGameId)).thenReturn(Optional.ofNullable(currentGameEntity));
//            when(gameRepositoryMock.findById(currentGameEntity.getGameId())).thenReturn(Optional.ofNullable(gameEntity));
//
//            //Act
//            GameEntity currentGameEntity = currentGameService.getCurrentGameEntity();
//
//            //Assert
//            assertEquals(gameEntity, currentGameEntity);
//        }
//
//        @Test
//        void getCurrentGameEntity_fail_whenCurrentGameNotFound() {
//            //Arrange
//            when(currentGameRepositoryMock.findById(firstGameId)).thenThrow(new ResourceNotFoundException("Resource not found"));
//
//            //Act
//            //Assert
//            assertThrows(ResourceNotFoundException.class, () -> currentGameService.getCurrentGameEntity());
//        }
//
//        @Test
//        void getCurrentGameEntity_fail_whenGameNotFound() {
//            //Arrange
//            when(currentGameRepositoryMock.findById(firstGameId)).thenReturn(Optional.ofNullable(currentGameEntity));
//            when(gameRepositoryMock.findById(currentGameEntity.getGameId())).thenThrow(new ResourceNotFoundException("Resource not found"));
//
//            //Act
//            //Assert
//            assertThrows(ResourceNotFoundException.class, () -> currentGameService.getCurrentGameEntity());
//        }
//
//    }
//
//    @Nested
//    class GetCurrentGame {
//
//        @Test
//        void getCurrentGame_success() {
//            //Arrange
//            when(gameMapperMock.toResponse(gameEntity)).thenReturn(gameResponseDto);
//            when(currentGameRepositoryMock.findById(firstGameId)).thenReturn(Optional.ofNullable(currentGameEntity));
//            when(gameRepositoryMock.findById(currentGameEntity.getGameId())).thenReturn(Optional.ofNullable(gameEntity));
//
//            //Act
//            GameResponseDto resultDto = currentGameService.getCurrentGame();
//
//            //Assert
//            assertEquals(gameResponseDto, resultDto);
//        }
//
//        @Test
//        void getCurrentGame_fail_whenCurrentGameNotFound() {
//            //Arrange
//            when(currentGameRepositoryMock.findById(firstGameId)).thenReturn(Optional.ofNullable(currentGameEntity));
//            when(gameRepositoryMock.findById(currentGameEntity.getGameId())).thenThrow(new ResourceNotFoundException("Resource not found"));
//
//            //Act
//            //Assert
//            assertThrows(ResourceNotFoundException.class, () -> currentGameService.getCurrentGame());
//        }
//
//        @Test
//        void getCurrentGame_fail_whenGameNotFound() {
//            //Arrange
//            when(currentGameRepositoryMock.findById(firstGameId)).thenThrow(new ResourceNotFoundException("Resource not found"));
//
//            //Act
//            //Assert
//            assertThrows(ResourceNotFoundException.class, () -> currentGameService.getCurrentGame());
//        }
//
//    }
//
//    @Nested
//    class MoveSnake {
//
//        @Test
//        void moveSnake_success() {
//            //Arrange
//            MoveSnakeEnum moveSnakeEnum = mock(MoveSnakeEnum.class);
//            when(currentGameRepositoryMock.findById(firstGameId)).thenReturn(Optional.ofNullable(currentGameEntity));
//            when(gameRepositoryMock.findById(currentGameEntity.getGameId())).thenReturn(Optional.ofNullable(gameEntity));
//            doNothing().when(moveSnakeEnum).move(game);
//            when(gameMapperMock.fromDomain(game)).thenReturn(gameEntity);
//            when(gameRepositoryMock.save(gameEntity)).thenReturn(gameEntity);
//            when(gameMapperMock.toResponse(gameEntity)).thenReturn(gameResponseDto);
//
//            try {
//                when(gameMapperMock.toGameDomain(gameEntity)).thenReturn(game);
//            } catch (Exception e) {
//                assert false;
//            }
//
//            //Act
//            GameResponseDto responseDto = currentGameService.moveSnake(moveSnakeEnum);
//
//            //Assert
//            assertEquals(gameResponseDto, responseDto);
//        }
//
//        @Test
//        void moveSnake_fail_whenGameNotFound() {
//            //Arrange
//            MoveSnakeEnum moveSnakeEnum = mock(MoveSnakeEnum.class);
//            when(currentGameRepositoryMock.findById(firstGameId)).thenThrow(new ResourceNotFoundException("Resource not found"));
//
//            //Act
//            //Assert
//            assertThrows(ResourceNotFoundException.class, () -> currentGameService.moveSnake(moveSnakeEnum));
//        }
//
//        @Test
//        void moveSnake_fail_cantMoveSnake() {
//            //Arrange
//            MoveSnakeEnum moveSnakeEnum = mock(MoveSnakeEnum.class);
//            when(currentGameRepositoryMock.findById(firstGameId)).thenReturn(Optional.ofNullable(currentGameEntity));
//            when(gameRepositoryMock.findById(currentGameEntity.getGameId())).thenReturn(Optional.ofNullable(gameEntity));
//            doThrow(new SnakeOutOfBoundsException("Snake out of bounds")).when(moveSnakeEnum).move(game);
//
//            try {
//                when(gameMapperMock.toGameDomain(gameEntity)).thenReturn(game);
//            } catch (Exception e) {
//                assert false;
//            }
//
//            //Act
//            //Assert
//            assertThrows(SnakeOutOfBoundsException.class, () -> currentGameService.moveSnake(moveSnakeEnum));
//        }
//
//        @Test
//        void moveSnake_fail_cantMap() {
//            //Arrange
//            MoveSnakeEnum moveSnakeEnum = mock(MoveSnakeEnum.class);
//            when(currentGameRepositoryMock.findById(firstGameId)).thenReturn(Optional.ofNullable(currentGameEntity));
//            when(gameRepositoryMock.findById(currentGameEntity.getGameId())).thenReturn(Optional.ofNullable(gameEntity));
//            doNothing().when(moveSnakeEnum).move(game);
//            when(gameMapperMock.fromDomain(game)).thenReturn(gameEntity);
//            when(gameRepositoryMock.save(gameEntity)).thenReturn(gameEntity);
//
//            try {
//                when(gameMapperMock.toGameDomain(gameEntity)).thenReturn(game);
//            } catch (Exception e) {
//                assert false;
//            }
//            when(gameMapperMock.toResponse(gameEntity)).thenThrow(new GameMappingException("Game mapping exception"));
//
//            //Act
//            //Assert
//            assertThrows(GameMappingException.class, () -> currentGameService.moveSnake(moveSnakeEnum));
//        }
//
//    }
//
//    @Nested
//    class UpdateGameInDb {
//
//        @Test
//        void updateGameInDb_success() {
//            //Arrange
//            when(gameMapperMock.fromDomain(game)).thenReturn(gameEntity);
//            when(gameRepositoryMock.save(gameEntity)).thenReturn(gameEntity);
//
//            //Act
//            GameEntity resultEntity = currentGameService.updateGameInDB(gameId, game);
//
//            //Assert
//            assertEquals(gameEntity, resultEntity);
//        }
//
//        @Test
//        void updateGameInDb_fail_cantMap() {
//            //Arrange
//            when(gameMapperMock.fromDomain(game)).thenThrow(new GameMappingException("Game mapping exception"));
//
//            //Act
//            //Assert
//            assertThrows(GameMappingException.class, () -> currentGameService.updateGameInDB(gameId, game));
//        }
//
//    }
//
//}