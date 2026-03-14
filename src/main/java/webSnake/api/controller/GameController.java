package webSnake.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import webSnake.api.dto.CreateGameDto;
import webSnake.api.dto.GameResponseDto;
import webSnake.api.validator.GameValidator;
import webSnake.service.GameService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/games")
@AllArgsConstructor
@Log4j2
public class GameController {

    private final GameService gameService;
    private final GameValidator gameValidator;

    @PostMapping("/create")
    public ResponseEntity<GameResponseDto> createGame(@RequestBody CreateGameDto request) {
        log.info("createGame started. Request: {}", request);
        gameValidator.validateCreationRequest(request);
        GameResponseDto newGame = gameService.createGame(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newGame.getGameId())
                .toUri();
        log.info("createGame ended.  newGame: {}", newGame);

        return ResponseEntity.created(location).body(newGame);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long gameId) {
        log.info("deleteGame started. gameId: {}", gameId);
        gameService.deleteGame(gameId);
        log.info("deleteGame ended.  gameId: {}", gameId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<GameResponseDto>> getAllGames() {
        log.info("getAllGames started.");
        List<GameResponseDto> all = gameService.getAllGames();
        log.info("getAllGames ended.");

        return ResponseEntity.ok(all);
    }

}