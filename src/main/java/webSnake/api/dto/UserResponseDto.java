package webSnake.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long userId;
    private String username;
    private String creationTime;
    private String lastPlayed;
    private Long currentGameId;

}