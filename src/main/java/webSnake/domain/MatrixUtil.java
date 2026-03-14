package webSnake.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import webSnake.exception.GameMappingException;

import java.awt.*;

@Component
@Log4j2
public final class MatrixUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Named("toJson")
    public static String toJson(int[][] matrix) throws JsonProcessingException {
        log.debug("toJson called");

        return MAPPER.writeValueAsString(matrix);
    }

    @Named("fromJson")
    public static int[][] fromJson(String json) throws Exception {
        log.debug("fromJson called");

        return MAPPER.readValue(json, int[][].class);
    }

    @Named("matrixToString")
    public static String matrixToString(int rowSize, int colSize, Point snakeLocation, Point appleLocation) {
        String board;
        int[][] matrix = new int[rowSize][colSize];
        matrix[snakeLocation.x][snakeLocation.y] = CellValueEnum.SNAKE.getValue();
        matrix[appleLocation.x][appleLocation.y] = CellValueEnum.APPLE.getValue();

        try {
            board = MAPPER.writeValueAsString(matrix);
        } catch (JsonProcessingException e) {
            throw new GameMappingException("Could not write matrix to string");
        }

        return board;
    }


}