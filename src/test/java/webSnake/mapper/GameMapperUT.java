package webSnake.mapper;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import webSnake.api.dto.GameResponseDto;
import webSnake.domain.Game;
import webSnake.entity.GameEntity;
import webSnake.entity.PointEmbed;
import webSnake.entity.UserEntity;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameMapperUT {

    private final GameMapper mapper = Mappers.getMapper(GameMapper.class);

    Long gameId = 100L;
    String board = "[[1,0,0,0],[0,2,0,0],[0,0,0,0],[0,0,0,0]]";
    int[][] matrix = {{1, 0, 0, 0}, {0, 2, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};
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
    UserEntity userEntity = UserEntity.builder().userId(userId).build();
    GameEntity entity = GameEntity.builder().snakeLocation(snakePointEmb).appleLocation(applePointEmb).gameId(gameId).score(score).colSize(colSize).rowSize(rowSize).owner(userEntity).build();

    @Nested
    class toResponse {

        @Test
        void toResponse_success() {
            //Arrange
            GameResponseDto expected = GameResponseDto.builder().gameId(gameId).board(board).score(score).build();

            //Act
            GameResponseDto response = mapper.toResponse(entity);

            //Assert
            assertEquals(expected.getGameId(), response.getGameId());
            assertEquals(expected.getBoard(), response.getBoard());
            assertEquals(expected.getScore(), response.getScore());
        }

    }

    @Nested
    class toGameDomain {

        @Test
        void toGameDomain_success() {
            //Arrange
            //Act
            try {
                Game gameResult = mapper.toGameDomain(entity);

                //Assert
                assertTrue(game.equals(gameResult));
            } catch (Exception e) {
                assert false;
            }
        }

    }

    @Nested
    class FromDomain {

        @Test
        void fromDomain_success() {
            //Arrange
            //Act
            GameEntity result = mapper.fromDomain(game);
            result.setGameId(entity.getGameId());

            //Assert
            assertTrue(entity.equals(result));
        }

    }

}