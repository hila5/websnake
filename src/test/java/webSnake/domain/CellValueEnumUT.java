package webSnake.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CellValueEnumUT {

    @Test
    void snake_hasCorrectValue() {
        assertEquals(1, CellValueEnum.SNAKE.getValue());
    }

    @Test
    void apple_hasCorrectValue() {
        assertEquals(2, CellValueEnum.APPLE.getValue());
    }

}