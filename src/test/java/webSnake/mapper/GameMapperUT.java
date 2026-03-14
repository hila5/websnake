//package webSnake.mapper;
//
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mapstruct.factory.Mappers;
//import org.mockito.junit.jupiter.MockitoExtension;
//import webSnake.api.dto.GameResponseDto;
//import webSnake.domain.Game;
//import webSnake.entity.GameEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@ExtendWith(MockitoExtension.class)
//public class GameMapperUT {
//
//    private final GameMapper mapper = Mappers.getMapper(GameMapper.class);
//
//    Long gameId = 100L;
//    String matrixJson = "[[1,2],[3,4]]";
//    int[][] matrix = {{1, 2}, {3, 4}};
//    int snakeX = 5;
//    int snakeY = 5;
//    int appleX = 5;
//    int appleY = 5;
//    int score = 10;
//    Game game = new Game(matrix, snakeX, snakeY, appleX, appleY, score);
//
//    GameEntity entity = GameEntity.builder().gameId(gameId).matrixJson(matrixJson).snakeX(snakeX).snakeY(snakeY).appleX(appleX).appleY(appleY).score(score).build();
//
//
//    @Nested
//    class toResponse {
//
//        @Test
//        void toResponse_success() {
//            //Arrange
//            GameResponseDto expected = GameResponseDto.builder().gameId(gameId).board(matrixJson).score(score).build();
//
//            //Act
//            GameResponseDto response = mapper.toResponse(entity);
//
//            //Assert
//            assertEquals(expected.getGameId(), response.getGameId());
//            assertEquals(expected.getBoard(), response.getBoard());
//            assertEquals(expected.getScore(), response.getScore());
//        }
//
//    }
//
//    @Nested
//    class toGameDomain {
//
//        @Test
//        void toGameDomain_success() {
//            //Arrange
//            //Act
//            try {
//                Game gameResult = mapper.toGameDomain(entity);
//
//                //Assert
//                assertTrue(game.equals(gameResult));
//            } catch (Exception e) {
//                assert false;
//            }
//        }
//
//    }
//
//    @Nested
//    class FromDomain {
//
//        @Test
//        void fromDomain_success() {
//            //Arrange
//            //Act
//            GameEntity result = mapper.fromDomain(game);
//            result.setGameId(entity.getGameId());
//
//            //Assert
//            assertTrue(entity.equals(result));
//        }
//
//    }
//
//}