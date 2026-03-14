package webSnake.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import webSnake.api.dto.*;
import webSnake.service.UserService;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@Log4j2
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody CreateUserDto createUserDto) {
        log.info("createUser started. createUserDto: {}", createUserDto);

        UserResponseDto userResponseDto = userService.createUser(createUserDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userResponseDto.getUserId())
                .toUri();

        log.info("createUser ended. createUserDto: {}", createUserDto);

        return ResponseEntity.created(location).body(userResponseDto);
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String identifier) {
        log.info("getUser started. getUser: {}", identifier);

        UserResponseDto userResponseDto = userService.getUserByIdOrUsername(identifier);

        log.info("getUser ended. getUser: {}", identifier);

        return ResponseEntity.ok(userResponseDto);
    }

    @GetMapping("/score/{identifier}")
    public ResponseEntity<UserSumScoreDto> getUserScore(@PathVariable String identifier) {
        log.info("getUserScore started. getUserScore: {}", identifier);

        UserSumScoreDto userSumScoreDto = userService.getScore(identifier);

        log.info("getUserScore ended. getUserScore: {}", identifier);

        return ResponseEntity.ok(userSumScoreDto);
    }

    @GetMapping("/rank/{identifier}")
    public ResponseEntity<UserRankDto> getUserRank(@PathVariable("identifier") String identifier) {
        log.info("getUserRank started. identifier: {}", identifier);

        UserRankDto userRankDto = userService.getScoreRank(identifier);

        log.info("getUserRank ended. identifier: {}", identifier);

        return ResponseEntity.ok(userRankDto);
    }

    @DeleteMapping("/{identifier}")
    public ResponseEntity<Void> deleteUser(@PathVariable("identifier") String identifier) {
        log.info("deleteUser started. deleteUser: {}", identifier);

        userService.deleteUser(identifier);

        log.info("deleteUser ended. deleteUser: {}", identifier);

        return ResponseEntity.noContent().build();

    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("getAllUsers started.");

        List<UserResponseDto> allUsers = userService.getAllUsers();

        log.info("getAllUsers ended.");

        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/allGames/{identifier}")
    public ResponseEntity<List<GameResponseDto>> getAllGames(@PathVariable("identifier") String identifier) {
        log.info("getAllGames started. identifier: {}", identifier);

        List<GameResponseDto> allUserGames = userService.getAllUserGames(identifier);

        log.info("getAllGames ended. identifier: {}", identifier);

        return ResponseEntity.ok(allUserGames);
    }

}