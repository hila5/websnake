package webSnake.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MoveSnakeDto {

    String userIdentifier;
    String action;

}