package webSnake.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MatrixUtilUT {

    @Nested
    class toJson {

        @Test
        public void toJson_success() {
            //Arrange
            int[][] matrix = new int[][]{{1, 2, 3}, {4, 5, 6}};
            String expected = "[[1,2,3],[4,5,6]]";

            try {
                //Act
                //Assert
                String matrixString = MatrixUtil.toJson(matrix);
                assertEquals(expected, matrixString);
            } catch (JsonProcessingException e) {
                assert false;
            }
        }

    }

    @Nested
    class fromJson {

        @Test
        public void fromJson_success() {
            //Arrange
            int[][] expectedMatrix = new int[][]{{1, 2, 3}, {4, 5, 6}};
            String json = "[[1,2,3],[4,5,6]]";

            //Act
            try {
                int[][] resultMatrix = MatrixUtil.fromJson(json);

                //Assert
                assertArrayEquals(expectedMatrix, resultMatrix);
            } catch (Exception e) {
                assert false;
            }
        }

    }

    @Nested
    class MatrixToString {

        @Test
        void matrixToString_success() throws Exception {
            // Arrange
            int rowSize = 4;
            int colSize = 4;
            Point snakeLocation = new Point(0, 0);
            Point appleLocation = new Point(1, 1);

            // Act
            String result = MatrixUtil.matrixToString(rowSize, colSize, snakeLocation, appleLocation);

            // Assert
            assertEquals("[[1,0,0,0],[0,2,0,0],[0,0,0,0],[0,0,0,0]]", result);
        }

        @Test
        void matrixToString_snakeAndAppleSameLocation_appleOverridesSnake() throws Exception {
            // Arrange
            int rowSize = 4;
            int colSize = 4;
            Point snakeLocation = new Point(0, 0);
            Point appleLocation = new Point(0, 0);

            // Act
            String result = MatrixUtil.matrixToString(rowSize, colSize, snakeLocation, appleLocation);

            // Assert
            assertEquals("[[2,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0]]", result);
        }

        @Test
        void matrixToString_snakeOutOfBounds_throwsException() {
            // Arrange
            int rowSize = 4;
            int colSize = 4;
            Point snakeLocation = new Point(10, 10);
            Point appleLocation = new Point(1, 1);

            // Act & Assert
            assertThrows(Exception.class,
                    () -> MatrixUtil.matrixToString(rowSize, colSize, snakeLocation, appleLocation));
        }

    }

}