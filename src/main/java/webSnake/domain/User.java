package webSnake.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Log4j2
public class User {

    private Long userId;
    private String username;
    private String creationTime;
    private String lastPlayed;
    private List<Game> games;

    public User(String username) {
        this.username = username;
        creationTime = LocalDateTime.now().toString();
    }

}