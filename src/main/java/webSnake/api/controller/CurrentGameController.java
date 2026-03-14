package webSnake.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webSnake.api.dto.GameResponseDto;
import webSnake.api.dto.MoveSnakeDto;
import webSnake.api.validator.PlayValidator;
import webSnake.domain.MoveSnakeEnum;
import webSnake.service.CurrentGameService;

@RestController
@RequestMapping("/current")
@AllArgsConstructor
@Log4j2
public class CurrentGameController {

    private final CurrentGameService currentGameService;
    private final PlayValidator playValidator;

    @PutMapping("/{userIdentifier}/{gameId}")
    public ResponseEntity<GameResponseDto> setUserCurrentGame(@PathVariable("userIdentifier") String userIdentifier, @PathVariable("gameId") Long gameId) {
        log.info("setUserCurrentGame started. userId:{},gameId: {} ", gameId, userIdentifier);

        GameResponseDto currentGameResponse = currentGameService.setUserCurrentGame(userIdentifier, gameId);

        log.info("setUserCurrentGame ended. userId:{},gameId: {} ", gameId, userIdentifier);

        return ResponseEntity.ok(currentGameResponse);
    }

    @GetMapping("/{userIdentifier}")
    public ResponseEntity<GameResponseDto> getUserCurrentGame(@PathVariable("userIdentifier") String userIdentifier) {
        log.info("getCurrentGame started.");

        GameResponseDto currentGameResponse = currentGameService.getUserCurrentGame(userIdentifier);

        log.info("getCurrentGame ended.");

        return ResponseEntity.ok(currentGameResponse);
    }

    @PostMapping("/moveSnake")
    public ResponseEntity<GameResponseDto> moveSnakeInCurrentGame(@RequestBody MoveSnakeDto request) {
        log.info("moveSnakeInCurrentGame started. request: {}", request);

        MoveSnakeEnum moveSnakeEnum = playValidator.getChosenMoveEnum(request);
        GameResponseDto changedGame = currentGameService.moveSnake(request.getUserIdentifier(), moveSnakeEnum);

        log.info("moveSnakeInCurrentGame ended. changedGame: {}", changedGame);

        return ResponseEntity.ok(changedGame);
    }

}