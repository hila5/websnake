package webSnake.api.validator;

import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import webSnake.api.dto.MoveSnakeDto;
import webSnake.domain.MoveSnakeEnum;

@Component
@Log4j2
public class PlayValidator {

    public MoveSnakeEnum getChosenMoveEnum(MoveSnakeDto moveSnakeDto) {
        log.debug("getChosenMoveEnum started.");
        if (moveSnakeDto.getAction() == null)
            throw new ValidationException("action is null");

        String action = moveSnakeDto.getAction().trim().toUpperCase();

        try {
            MoveSnakeEnum moveSnakeEnum = MoveSnakeEnum.valueOf(action);
            log.debug("getChosenMoveEnum ended. moveSnakeEnum:{}", moveSnakeEnum.name());

            return moveSnakeEnum;
        } catch (Exception e) {
            throw new ValidationException("Invalid action");
        }
    }

}