package webSnake.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webSnake.api.dto.GameResponseDto;
import webSnake.domain.Game;
import webSnake.domain.MoveSnakeEnum;
import webSnake.entity.GameEntity;
import webSnake.entity.UserEntity;
import webSnake.exception.GameMappingException;
import webSnake.exception.NotYoursGameException;
import webSnake.exception.ResourceNotFoundException;
import webSnake.exception.SnakeOutOfBoundsException;
import webSnake.mapper.GameMapper;
import webSnake.repositories.GameRepository;
import webSnake.repositories.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class CurrentGameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final GameMapper gameMapper;
    private final UserService userService;

    public GameResponseDto setUserCurrentGame(String userIdentifier, Long gameId) {
        log.info("setUserCurrentGame started. userIdentifier:{}, gameId:{} ", userIdentifier, gameId);

        UserEntity userEntity = userService.getUserEntityByIdentifier(userIdentifier);
        GameEntity gameEntity = gameRepository.findById(gameId).
                orElseThrow(() -> new ResourceNotFoundException("Game id= " + gameId + " not found"));

        if (!gameEntity.getOwner().equals(userEntity)) {
            throw new NotYoursGameException("You are not the owner of this game sweetheart");
        }

        userEntity.setCurrentGameId(gameId);
        userRepository.save(userEntity);

        log.info("setUserCurrentGame ended. userIdentifier:{}, gameId:{} ", userIdentifier, gameId);

        return gameMapper.toResponse(gameEntity);
    }

    public GameEntity getUserCurrentGameEntity(String identifier) {
        log.info("getUserCurrentGame started.");

        UserEntity userEntity = userService.getUserEntityByIdentifier(identifier);
        Long gameId = userEntity.getCurrentGameId();

        if (gameId == null) {
            throw new ResourceNotFoundException("The user identifier " + identifier + " does't has any game");
        }

        Optional<GameEntity> currentGameEntity = gameRepository.findById(gameId);

        if (currentGameEntity.isPresent()) {
            log.info("getUserCurrentGame ended.");

            return currentGameEntity.get();
        } else {
            throw new ResourceNotFoundException("user: " + identifier + " doesnt has a current game");
        }
    }

    public GameResponseDto getUserCurrentGame(String identifier) {
        log.info("getUserCurrentGame started. identifier:{}", identifier);

        GameResponseDto gameResponseDto = gameMapper.toResponse(getUserCurrentGameEntity(identifier));

        log.info("getUserCurrentGame ended. identifier:{},gameId:{}", identifier, gameResponseDto.getGameId());

        return gameResponseDto;
    }

    public GameResponseDto moveSnake(String userIdentifier, MoveSnakeEnum moveSnakeEnum) {
        log.info("moveSnake started. userIdentifier:{}, moveSnakeEnum:{}", userIdentifier, moveSnakeEnum.name());

        GameEntity gameEntity = getUserCurrentGameEntity(userIdentifier);

        try {
            Game game = gameMapper.toGameDomain(gameEntity);
            moveSnakeEnum.move(game);
            GameEntity savedEntity = updateGameInDB(gameEntity, game);
            log.info("moveSnake ended. userIdentifier:{}, gameId:{} ,moveSnakeEnum:{}", userIdentifier, gameEntity.getGameId(), moveSnakeEnum.name());

            return gameMapper.toResponse(savedEntity);
        } catch (SnakeOutOfBoundsException snakeOutOfBoundsException) {
            log.warn("moveSnake failed. error:{}", snakeOutOfBoundsException.getMessage());

            throw snakeOutOfBoundsException;
        } catch (Exception e) {
            log.error("moveSnake failed. error:{}", e.getMessage());

            throw new GameMappingException(e.getMessage());
        }
    }

    public GameEntity updateGameInDB(GameEntity gameEntity, Game updatedGame) {
        log.info("updateGameInDB started.");
        long gameId = gameEntity.getGameId();
        GameEntity updatedGameEntity;

        try {
            updatedGameEntity = gameMapper.fromDomain(updatedGame);
            updatedGameEntity.setGameId(gameId);
            updatedGameEntity.setOwner(gameEntity.getOwner());
            userService.updateLastPlayedForUser(gameEntity.getOwner());
            log.info("updateGameInDB ended. gameId:{}", gameId);

            return gameRepository.save(updatedGameEntity);
        } catch (Exception e) {
            log.warn("updateGameInDB failed. error:{}", e.getMessage());
            throw new GameMappingException(e.getMessage());
        }
    }

}