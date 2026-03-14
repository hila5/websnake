package webSnake.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

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


}