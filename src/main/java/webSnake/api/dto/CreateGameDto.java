package webSnake.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateGameDto {

    private String userIdentifier;
    private Integer rows;
    private Integer cols;

}