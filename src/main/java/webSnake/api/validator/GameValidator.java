package webSnake.api.validator;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import webSnake.api.dto.CreateGameDto;

@Slf4j
@Component
public class GameValidator {

    private final int MIN_LENGTH = 4;
    private final int MAX_LENGTH = 10;

    public void validateCreationRequest(CreateGameDto createGameDto) {
        log.debug("validateCreationRequest started.");

        if (createGameDto.getRows() == null || createGameDto.getCols() == null || createGameDto.getUserIdentifier() == null) {

            log.warn("validateCreationRequest failed.");

            throw new ValidationException("some fields are missing.");
        }

        validateValue("rows", createGameDto.getRows());
        validateValue("cols", createGameDto.getCols());

        log.debug("validateCreationRequest finished.");
    }

    private void validateValue(String name, int value) {
        if (value < MIN_LENGTH || value > MAX_LENGTH) {

            log.warn("validateCreationRequest failed.");

            throw new ValidationException(name + " must be between: " + MIN_LENGTH + " to " + MAX_LENGTH);
        }
    }

}