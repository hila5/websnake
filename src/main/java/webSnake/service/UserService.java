package webSnake.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webSnake.api.dto.*;
import webSnake.domain.User;
import webSnake.entity.GameEntity;
import webSnake.entity.UserEntity;
import webSnake.exception.ResourceNotFoundException;
import webSnake.exception.UserNameUnavailable;
import webSnake.mapper.GameMapper;
import webSnake.mapper.UserMapper;
import webSnake.repositories.GameRepository;
import webSnake.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserMapper userMapper;
    private final GameMapper gameMapper;

    public UserResponseDto createUser(CreateUserDto createUserDto) {
        log.info("createUser started." + createUserDto);

        String username = createUserDto.getUsername();
        checkIfUserNameExist(username);
        User user = new User(username);
        UserEntity userEntity = userMapper.fromDomain(user);
        userRepository.save(userEntity);

        log.info("createUser ended." + createUserDto);

        return userMapper.toResponse(userEntity);
    }

    private void checkIfUserNameExist(String username) {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity != null) {
            throw new UserNameUnavailable("the username " + username + " is already taken");
        }
    }

    public UserResponseDto getUserByIdOrUsername(String identifier) {
        log.info("getUserByIdOrUsername started." + identifier);

        UserEntity userEntity = getUserEntityByIdentifier(identifier);

        log.info("getUserByIdOrUsername ended." + identifier);

        return userMapper.toResponse(userEntity);
    }

    public UserEntity getUserEntityByIdentifier(String identifier) {
        log.debug("getUserEntityByIdentifier started." + identifier);

        List<Function<String, Optional<UserEntity>>> functions = List.of(findUserByIdFunction(), findUserByUsernameFunction());
        UserEntity userEntity = UserServiceUtil.findByFunctionList(identifier, functions).
                orElseThrow(() -> new ResourceNotFoundException("User with identifier " + identifier + " not found"));

        log.debug("getUserEntityByIdentifier ended." + identifier);

        return userEntity;
    }

    private Function<String, Optional<UserEntity>> findUserByIdFunction() {
        return identifier -> {
            if (UserServiceUtil.isLongParsable(identifier)) {
                long userId = Long.parseLong(identifier);

                return userRepository.findById(userId);
            }

            return Optional.empty();
        };
    }

    private Function<String, Optional<UserEntity>> findUserByUsernameFunction() {
        return identifier -> Optional.ofNullable(userRepository.findByUsername(identifier));
    }

    public void deleteUser(String identifier) {
        log.info("deleteUser started." + identifier);

        UserEntity userEntity = getUserEntityByIdentifier(identifier);
        userRepository.delete(userEntity);

        log.info("deleteUser ended." + identifier);
    }

    public List<UserResponseDto> getAllUsers() {
        log.info("getAllUsers started.");

        List<UserResponseDto> userResponseDtos = userRepository.findAll().stream().map(userMapper::toResponse).collect(Collectors.toList());

        log.info("getAllUsers ended.");

        return userResponseDtos;
    }

    public List<GameResponseDto> getAllUserGames(String identifier) {
        log.info("getAllUserGames started." + identifier);

        List<GameResponseDto> gameResponseDtos = getUserGamesEntities(identifier)
                .stream()
                .map(gameMapper::toResponse)
                .collect(Collectors.toList());

        log.info("getAllUserGames ended." + identifier);

        return gameResponseDtos;
    }

    private List<GameEntity> getUserGamesEntities(String identifier) {
        UserEntity userEntity = getUserEntityByIdentifier(identifier);
        return gameRepository.findByOwnerUserId(userEntity.getUserId());
    }

    public void updateLastPlayedForUser(UserEntity owner) {
        owner.setLastPlayed(LocalDateTime.now().toString());
        userRepository.save(owner);
    }

    public UserSumScoreDto getScore(String identifier) {
        UserEntity userEntity = getUserEntityByIdentifier(identifier);
        Long sumOfScores = gameRepository.sumScoresByUserId(userEntity.getUserId());

        if (sumOfScores == null) {
            sumOfScores = 0L;
        }

        return UserSumScoreDto.builder().sumScore(sumOfScores).build();

    }

    public UserRankDto getScoreRank(String identifier) {
        log.info("getScoreRank started." + identifier);

        UserEntity userEntity = getUserEntityByIdentifier(identifier);
        UserScoreRank userScoreRank = gameRepository.findRankForUser(userEntity.getUserId());
        UserRankDto userRankDto = UserRankDto.builder().rank(userScoreRank.getUserRank()).build();

        log.info("getScoreRank ended." + identifier);

        return userRankDto;
    }

}