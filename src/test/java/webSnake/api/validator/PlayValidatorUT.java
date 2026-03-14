package webSnake.api.validator;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import webSnake.api.dto.MoveSnakeDto;
import webSnake.domain.MoveSnakeEnum;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PlayValidatorUT {

    PlayValidator playValidator = new PlayValidator();

    @Nested
    class GetChosenMoveGame {

        @Nested
        class Success {

            @Test
            void getChosenMoveGame_up_success() {
                //Arrange
                MoveSnakeDto moveSnakeDto = MoveSnakeDto.builder().action("up").build();

                //Act
                MoveSnakeEnum result = playValidator.getChosenMoveEnum(moveSnakeDto);

                //Assert
                assert (result == MoveSnakeEnum.UP);
            }

            @Test
            void getChosenMoveGame_down_success() {
                //Arrange
                MoveSnakeDto moveSnakeDto = MoveSnakeDto.builder().action("down").build();

                //Act
                MoveSnakeEnum result = playValidator.getChosenMoveEnum(moveSnakeDto);

                //Assert
                assert (result == MoveSnakeEnum.DOWN);
            }

            @Test
            void getChosenMoveGame_right_success() {
                //Arrange
                MoveSnakeDto moveSnakeDto = MoveSnakeDto.builder().action("right").build();

                //Act
                MoveSnakeEnum result = playValidator.getChosenMoveEnum(moveSnakeDto);

                //Assert
                assert (result == MoveSnakeEnum.RIGHT);
            }

            @Test
            void getChosenMoveGame_left_success() {
                //Arrange
                MoveSnakeDto moveSnakeDto = MoveSnakeDto.builder().action("left").build();

                //Act
                MoveSnakeEnum result = playValidator.getChosenMoveEnum(moveSnakeDto);

                //Assert
                assert (result == MoveSnakeEnum.LEFT);
            }
        }

        @Nested
        class fail {

            @Test
            void getChosenMoveGame_invalidStringInput_fail() {
                //Arrange
                MoveSnakeDto moveSnakeDto = MoveSnakeDto.builder().action("hila").build();

                //Act
                //Assert
                assertThrows(jakarta.validation.ValidationException.class, () -> playValidator.getChosenMoveEnum(moveSnakeDto));

            }

            @Test
            void getChosenMoveGame_nullInput_fail() {
                //Arrange
                MoveSnakeDto moveSnakeDto = MoveSnakeDto.builder().action(null).build();

                //Act
                //Assert
                assertThrows(jakarta.validation.ValidationException.class, () -> playValidator.getChosenMoveEnum(moveSnakeDto));

            }

        }

    }

}