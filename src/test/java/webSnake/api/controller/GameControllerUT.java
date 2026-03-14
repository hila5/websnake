package webSnake.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import webSnake.api.dto.CreateGameDto;
import webSnake.api.dto.GameResponseDto;
import webSnake.api.validator.GameValidator;
import webSnake.exception.GlobalExceptionHandler;
import webSnake.exception.ResourceNotFoundException;
import webSnake.service.GameService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(GlobalExceptionHandler.class)
@WebMvcTest(controllers = GameController.class)
public class GameControllerUT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameServiceMock;

    @MockBean
    private GameValidator gameValidatorMock;

    @Autowired
    private ObjectMapper objectMapper;

    private long gameId = 45;

    private GameResponseDto sampleGameResponseDto() {
        return GameResponseDto.builder().gameId(10L)
                .board("[[0,0,0,0,0],[0,0,2,0,0],[0,0,0,1,0],[0,0,0,0,0]]")
                .score(2)
                .build();
    }

    private CreateGameDto requestCreateGameDto() {
        return CreateGameDto.builder().rows(4).cols(4).build();
    }

    private CreateGameDto badRequestCreateGameDto() {
        return CreateGameDto.builder().rows(1).cols(11).build();
    }

    @Nested
    class CreateGame {

        @Test
        void createGame_success() {
            //Arrange
            CreateGameDto requestCreateGameDto = requestCreateGameDto();
            GameResponseDto gameResponseDto = sampleGameResponseDto();
            doNothing().when(gameValidatorMock).validateCreationRequest(requestCreateGameDto);
            when(gameServiceMock.createGame(any(CreateGameDto.class))).thenReturn(gameResponseDto);

            //Act
            //Assert
            try {
                mockMvc.perform(post("/games/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestCreateGameDto)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.gameId").value(gameResponseDto.getGameId()))
                        .andExpect(jsonPath("$.board").value(gameResponseDto.getBoard()))
                        .andExpect(jsonPath("$.score").value(gameResponseDto.getScore()));
            } catch (Exception e) {
                assert false;
            }
        }

        @Test
        void createGame_failure() {
            //Arrange
            CreateGameDto badRequestCreateGameDto = badRequestCreateGameDto();
            doThrow(new ValidationException("bad values")).when(gameValidatorMock).validateCreationRequest(any(CreateGameDto.class));

            //Act
            //Assert
            try {
                mockMvc.perform(post("/games/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(badRequestCreateGameDto)))
                        .andExpect(status().isBadRequest())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
            } catch (Exception e) {
                assert false;
            }
        }

    }

    @Nested
    class deleteGame {

        @Test
        void deleteGame_success() {
            //Arrange
            doNothing().when(gameServiceMock).deleteGame(gameId);

            //Act
            //Assert
            try {
                mockMvc.perform(delete("/games/{gameId}", gameId)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());
            } catch (Exception e) {
                assert false;
            }
        }

        @Test
        void deleteGame_failure() {
            //Arrange
            doThrow(new ResourceNotFoundException("game " + gameId + " not found")).when(gameServiceMock).deleteGame(gameId);

            //Act
            //Assert
            try {
                mockMvc.perform(delete("/games/{gameId}", gameId)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            } catch (Exception e) {
                assert false;
            }
        }

    }

    @Nested
    class getAllGames {

        @Test
        void getAllGames_success() {
            //Arrange
            List<GameResponseDto> gameResponseDtos = List.of(sampleGameResponseDto());
            when(gameServiceMock.getAllGames()).thenReturn(gameResponseDtos);
            int expectedSize = gameResponseDtos.size();

            //Act
            //Assert
            try {
                mockMvc.perform(get("/games")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$.length()").value(expectedSize));
            } catch (Exception e) {
                assert false;
            }
        }

        @Test
        void getAllGames_emptyList_success() {
            List<GameResponseDto> gameResponseDtos = List.of();
            when(gameServiceMock.getAllGames()).thenReturn(gameResponseDtos);
            int expectedSize = gameResponseDtos.size();

            //Act
            //Assert
            try {
                mockMvc.perform(get("/games")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$.length()").value(expectedSize));
            } catch (Exception e) {
                assert false;
            }
        }

    }

}