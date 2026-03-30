package webSnake.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webSnake.api.dto.CreateGameDto;
import webSnake.api.dto.GameResponseDto;
import webSnake.domain.Game;
import webSnake.entity.GameEntity;
import webSnake.entity.UserEntity;
import webSnake.exception.CurrentGameDeletedException;
import webSnake.exception.ResourceNotFoundException;
import webSnake.mapper.GameMapper;
import webSnake.repositories.GameRepository;
import webSnake.repositories.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
public class GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final CurrentGameService currentGameService;
    private final UserService userService;
    private final UserRepository userRepository;


    public GameResponseDto createGame(CreateGameDto gameDto) {
        log.info("createGame started. userIdentifier:{},rows:{}, cols:{}", gameDto.getUserIdentifier(), gameDto.getRows(), gameDto.getCols());

        int rows = gameDto.getRows();
        int cols = gameDto.getCols();
        String userIdentifier = gameDto.getUserIdentifier();
        UserEntity userEntity = userService.getUserEntityByIdentifier(userIdentifier);
        Game newGame = new Game(rows, cols);
        GameEntity gameEntity = gameMapper.fromDomain(newGame);
        gameEntity.setOwner(userEntity);
        gameRepository.save(gameEntity);
        GameResponseDto responseDto = currentGameService.setUserCurrentGame(userIdentifier, gameEntity.getGameId());

        log.info("createGame finished. userIdentifier:{}, rows:{}, cols:{}", gameDto.getUserIdentifier(), rows, cols);

        return responseDto;
    }

    public List<GameResponseDto> getAllGames() {
        log.info("getAllGames started.");

        List<GameResponseDto> gameResponseDtos = gameRepository.findAll().stream().map(gameMapper::toResponse).collect(Collectors.toList());

        log.info("getAllGames finished.");

        return gameResponseDtos;
    }

    public void deleteGame(Long gameId) {
        log.info("deleteGame started. gameId:{}", gameId);

        if (!gameRepository.existsById(gameId)) {
            log.warn("deleteGame failed. gameId:{}", gameId);
            throw new ResourceNotFoundException("Game id=" + gameId + " not found");
        }

        checkGameUserCurrentGame(gameId);
        gameRepository.deleteById(gameId);

        log.info("deleteGame finished. gameId:{}", gameId);
    }

    private void checkGameUserCurrentGame(Long gameId) {
        UserEntity userEntity = userRepository.findByCurrentGameId(gameId);
        if (userEntity != null) {
            throw new CurrentGameDeletedException("Game id=" + gameId + " is current game of userId=" + userEntity.getUserId() + ". change the current game to another game.");
        }
    }

}