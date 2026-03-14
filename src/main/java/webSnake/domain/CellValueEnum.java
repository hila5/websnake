package webSnake.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CellValueEnum {

    SNAKE(1),
    APPLE(2);

    private final int value;

}