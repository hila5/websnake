package webSnake.api.validator;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import webSnake.api.dto.CreateGameDto;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class GameValidatorUT {

    private final GameValidator gameValidator = new GameValidator();
    private final String userIdentifier = "rafa";

    CreateGameDto dto(String userIdentifier,Integer rows, Integer cols) {
        return CreateGameDto.builder().userIdentifier(userIdentifier).rows(rows).cols(cols).build();
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class ValidateCreationRequest {

        Stream<CreateGameDto> validProvider() {
            return Stream.of(
                    dto(userIdentifier,4, 4),
                    dto(userIdentifier,10, 10),
                    dto(userIdentifier,4, 5),
                    dto(userIdentifier,5, 4),
                    dto(userIdentifier,9, 7)
            );
        }

        Stream<CreateGameDto> invalidProvider() {
            return Stream.of(
                    dto(userIdentifier,0, 4),
                    dto(userIdentifier,10, 0),
                    dto(userIdentifier,4, 20),
                    dto(userIdentifier,0, 0),
                    dto(userIdentifier,9, null),
                    dto(userIdentifier,null, 10)
            );
        }

        @ParameterizedTest
        @MethodSource("validProvider")
        void validateCreationRequest_valid(CreateGameDto createGameDto) {
            //Act
            //Assert
            assertDoesNotThrow(() -> gameValidator.validateCreationRequest(createGameDto));
        }

        @ParameterizedTest
        @MethodSource("invalidProvider")
        void validateCreationRequest_invalid(CreateGameDto createGameDto) {
            //Act
            //Assert
            assertThrows(jakarta.validation.ValidationException.class, () -> gameValidator.validateCreationRequest(createGameDto));
        }

    }

}