package webSnake.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import webSnake.exception.SnakeOutOfBoundsException;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GameUT {

    private final String UP = "UP";
    private final String DOWN = "DOWN";
    private final String LEFT = "LEFT";
    private final String RIGHT = "RIGHT";
    private final int rowSize = 4;
    private final int colSize = 4;
    private final Point snakeLocation = new Point(0, 0);
    private final Point appleLocation = new Point(1, 1);
    private final int score = 0;
    private Game game = new Game(rowSize, colSize, snakeLocation, appleLocation, score);


    @Test
    public void createGame_testAppleDifferentFromSnakeLocation(){
        //Arrange
        //Act
        Game game = new Game(rowSize, colSize);

        //Assert
        assertNotNull(game);
        assertNotEquals(game.getSnakeLocation(),game.getAppleLocation());
    }

    @Nested
    class MoveSnake {

        @Nested
        class MoveRight {

            @Test
            public void moveSnakeRight_success() {
                //Arrange
                game.setSnakeLocation(new Point(0, 0));
                Point expectedSnakeLocation = new Point(0, 1);

                //Act
                game.moveTheSnake(UP, snakeLocation.x, snakeLocation.y + 1);

                //Assert
                assertEquals(game.getSnakeLocation(), expectedSnakeLocation);
            }

            @Test
            public void moveSnakeRight_unableToMove() {
                //Arrange
                game.setSnakeLocation(new Point(3, 3));
                Point expectedSnakeLocation = new Point(3, 3);

                //Act
                //Assert
                assertThrows(SnakeOutOfBoundsException.class, () -> game.moveTheSnake(RIGHT, 4, 3));
                assertEquals(game.getSnakeLocation(), expectedSnakeLocation);
            }

        }

        @Nested
        class MoveLeft {

            @Test
            public void moveSnakeLeft_success() {
                //Arrange
                game.setSnakeLocation(new Point(1, 1));
                Point expectedSnakeLocation = new Point(1, 0);

                //Act
                game.moveTheSnake(LEFT, game.getSnakeLocation().x, game.getSnakeLocation().y - 1);

                //Assert
                assertEquals(game.getSnakeLocation(), expectedSnakeLocation);
            }

            @Test
            public void moveSnakeLeft_unableToMove() {
                //Arrange
                game.setSnakeLocation(new Point(0, 0));
                Point expectedSnakeLocation = game.getSnakeLocation();
                //Act
                //Assert
                assertThrows(SnakeOutOfBoundsException.class, () -> game.moveTheSnake(LEFT, 0, -1));
                assertEquals(expectedSnakeLocation, game.getSnakeLocation());
            }

        }

        @Nested
        class MoveUp {

            @Test
            public void moveSnakeUp_success() {
                //Arrange
                game.setSnakeLocation(new Point(1, 1));
                Point expectedSnakeLocation = new Point(0, 1);

                //Act
                game.moveTheSnake(UP, game.getSnakeLocation().x - 1, game.getSnakeLocation().y);

                //Assert
                assertEquals(game.getSnakeLocation(), expectedSnakeLocation);
            }

            @Test
            public void moveSnakeUp_unableToMove() {
                //Arrange
                game.setSnakeLocation(new Point(0, 0));
                Point expectedSnakeLocation = game.getSnakeLocation();
                //Act
                //Assert
                assertThrows(SnakeOutOfBoundsException.class, () -> game.moveTheSnake(UP, -1, 0));
                assertEquals(expectedSnakeLocation, game.getSnakeLocation());
            }

        }

        @Nested
        class MoveDown {

            @Test
            public void moveSnakeDown_success() {
                //Arrange
                game.setSnakeLocation(new Point(1, 1));
                Point expectedSnakeLocation = new Point(2, 1);

                //Act
                game.moveTheSnake(DOWN, game.getSnakeLocation().x + 1, game.getSnakeLocation().y);

                //Assert
                assertEquals(game.getSnakeLocation(), expectedSnakeLocation);
            }

            @Test
            public void moveSnakeDown_unableToMove() {
                //Arrange
                game.setSnakeLocation(new Point(3, 3));
                Point expectedSnakeLocation = game.getSnakeLocation();
                //Act
                //Assert
                assertThrows(SnakeOutOfBoundsException.class, () -> game.moveTheSnake(UP, 4, 3));
                assertEquals(expectedSnakeLocation, game.getSnakeLocation());
            }

        }

    }

    @Nested
    class MoveAndEatApple {

        @Test
        public void moveAndEatApple_success() {
            //Arrange
            game.setSnakeLocation(new Point(1, 0));
            game.setAppleLocation(appleLocation);
            Point expectedSnakeLocation = new Point(1, 1);
            Point beforeEatAppleLocation = game.getAppleLocation();
            int expectedScore = game.getScore() + 1;
            //Act
            game.moveTheSnake(RIGHT, game.getSnakeLocation().x, game.getSnakeLocation().y + 1);
            Point afterEatAppleLocation = game.getAppleLocation();

            //Assert
            assertEquals(game.getSnakeLocation(), expectedSnakeLocation);
            assertNotEquals(beforeEatAppleLocation, afterEatAppleLocation);
            assertEquals(expectedScore, game.getScore());
        }

    }

}