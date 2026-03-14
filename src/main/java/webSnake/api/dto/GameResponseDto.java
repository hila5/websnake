package webSnake.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameResponseDto {

    private String userId;
    private Long gameId;
    private String board;
    private int score;

}