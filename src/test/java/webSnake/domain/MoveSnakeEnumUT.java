package webSnake.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import webSnake.exception.SnakeOutOfBoundsException;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MoveSnakeEnumUT {

    @Mock
    Game game;
    Point snakeLocation = new Point(1, 1);

    @Nested
    class Up {

        @Test
        public void up_success() {
            //Arrange
            doNothing().when(game).moveTheSnake(any(), anyInt(), anyInt());
            when(game.getSnakeLocation()).thenReturn(snakeLocation);

            //Act
            //Assert
            assertDoesNotThrow(() -> MoveSnakeEnum.UP.move(game));
        }

        @Test
        public void up_failure() {
            //Arrange
            doThrow(new SnakeOutOfBoundsException("cant move up")).when(game).moveTheSnake(any(), anyInt(), anyInt());
            when(game.getSnakeLocation()).thenReturn(snakeLocation);

            //Act
            //Assert
            assertThrows(SnakeOutOfBoundsException.class, () -> MoveSnakeEnum.UP.move(game));
        }

    }

    @Nested
    class Down {

        @Test
        public void down_success() {
            //Arrange
            doNothing().when(game).moveTheSnake(any(), anyInt(), anyInt());
            when(game.getSnakeLocation()).thenReturn(snakeLocation);

            //Act
            //Assert
            assertDoesNotThrow(() -> MoveSnakeEnum.DOWN.move(game));
        }

        @Test
        public void down_failure() {
            //Arrange
            doThrow(new SnakeOutOfBoundsException("cant move down")).when(game).moveTheSnake(any(), anyInt(), anyInt());
            when(game.getSnakeLocation()).thenReturn(snakeLocation);

            //Act
            //Assert
            assertThrows(SnakeOutOfBoundsException.class, () -> MoveSnakeEnum.DOWN.move(game));
        }

    }

    @Nested
    class Right {

        @Test
        public void right_success() {
            //Arrange
            doNothing().when(game).moveTheSnake(any(), anyInt(), anyInt());
            when(game.getSnakeLocation()).thenReturn(snakeLocation);

            //Act
            //Assert
            assertDoesNotThrow(() -> MoveSnakeEnum.RIGHT.move(game));
        }

        @Test
        public void down_failure() {
            //Arrange
            doThrow(new SnakeOutOfBoundsException("cant move right")).when(game).moveTheSnake(any(), anyInt(), anyInt());
            when(game.getSnakeLocation()).thenReturn(snakeLocation);

            //Act
            //Assert
            assertThrows(SnakeOutOfBoundsException.class, () -> MoveSnakeEnum.RIGHT.move(game));
        }

    }

    @Nested
    class Left {

        @Test
        public void down_success() {
            //Arrange
            doNothing().when(game).moveTheSnake(any(), anyInt(), anyInt());
            when(game.getSnakeLocation()).thenReturn(snakeLocation);

            //Act
            //Assert
            assertDoesNotThrow(() -> MoveSnakeEnum.LEFT.move(game));
        }

        @Test
        public void down_failure() {
            //Arrange
            doThrow(new SnakeOutOfBoundsException("cant move left")).when(game).moveTheSnake(any(), anyInt(), anyInt());
            when(game.getSnakeLocation()).thenReturn(snakeLocation);

            //Act
            //Assert
            assertThrows(SnakeOutOfBoundsException.class, () -> MoveSnakeEnum.LEFT.move(game));
        }

    }

}