package webSnake.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ObjectFactory;
import org.mapstruct.ReportingPolicy;
import webSnake.api.dto.GameResponseDto;
import webSnake.domain.Game;
import webSnake.domain.MatrixUtil;
import webSnake.entity.GameEntity;
import webSnake.exception.GameMappingException;


@Mapper(
        imports = MatrixUtil.class,
        uses = {MatrixUtil.class, PointEmbedMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface GameMapper {

    @Mapping(target = "userId", source = "owner.userId")
    @Mapping(target = "gameId", source = "gameId")
    @Mapping(
            target = "board",
            expression = "java(MatrixUtil.matrixToString("
                    + "entity.getRowSize(), "
                    + "entity.getColSize(), "
                    + "entity.getSnakeLocation().toPoint(), "
                    + "entity.getAppleLocation().toPoint()))"
    )
    @Mapping(target = "score", source = "score")
    GameResponseDto toResponse(GameEntity entity);

    @Mapping(target = "rowSize", source = "rowSize")
    @Mapping(target = "colSize", source = "colSize")
    @Mapping(target = "snakeLocation", source = "snakeLocation")
    @Mapping(target = "appleLocation", source = "appleLocation")
    @Mapping(target = "score", source = "score")
    Game toGameDomain(GameEntity entity) throws Exception;

    @Mapping(target = "gameId", ignore = true)
    @Mapping(target = "rowSize", source = "rowSize")
    @Mapping(target = "colSize", source = "colSize")
    @Mapping(target = "snakeLocation", source = "snakeLocation")
    @Mapping(target = "appleLocation", source = "appleLocation")
    @Mapping(target = "score", source = "score")
    GameEntity fromDomain(Game game) throws GameMappingException;

    @ObjectFactory
    default Game createGame(GameEntity entity) {
        return new Game(
                entity.getRowSize(),
                entity.getColSize(),
                entity.getSnakeLocation().toPoint(),
                entity.getAppleLocation().toPoint(),
                entity.getScore());
    }

}