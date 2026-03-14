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
import webSnake.api.dto.GameResponseDto;
import webSnake.api.dto.MoveSnakeDto;
import webSnake.api.validator.PlayValidator;
import webSnake.domain.MoveSnakeEnum;
import webSnake.exception.GlobalExceptionHandler;
import webSnake.exception.ResourceNotFoundException;
import webSnake.service.CurrentGameService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CurrentGameController.class)
@Import(GlobalExceptionHandler.class)
public class CurrentGameControllerUT {

    @Autowired
    private MockMvc mockMvc;

    private long gameId = 45;
    private String userIdentifier = "amit";

    @MockBean
    private CurrentGameService currentGameServiceMock;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlayValidator playValidatorMock;

    private GameResponseDto sampleGameResponseDto() {
        return GameResponseDto.builder().gameId(gameId)
                .board("[[0,0,0,0,0],[0,0,2,0,0],[0,0,0,1,0],[0,0,0,0,0]]")
                .score(2)
                .userId(userIdentifier)
                .build();
    }

    private MoveSnakeDto requestMoveSnakeDto() {
        return MoveSnakeDto.builder().userIdentifier(userIdentifier).action("up").build();
    }

    @Nested
    class SetCurrentGame {

        @Test
        void setUserCurrentGame_success() {
            //arrange
            when(currentGameServiceMock.setUserCurrentGame(userIdentifier,gameId)).thenReturn(sampleGameResponseDto());
            GameResponseDto expected = sampleGameResponseDto();

            //act//assert
            try {
                mockMvc.perform(put("/current/{userIdentifier}/{gameId}", userIdentifier,gameId)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.userId").value(expected.getUserId()))
                        .andExpect(jsonPath("$.gameId").value(expected.getGameId()))
                        .andExpect(jsonPath("$.board").value(expected.getBoard()))
                        .andExpect(jsonPath("$.score").value(expected.getScore()));
            } catch (Exception e) {
                assert false;
            }
        }

        @Test
        void setUserCurrentGame_fail() {
            //Arrange
            when(currentGameServiceMock.setUserCurrentGame(userIdentifier,gameId)).thenThrow(new ResourceNotFoundException("Game id=" + gameId + " not found"));

            //Act
            //Assert
            try {
                mockMvc.perform(put("/current/{userIdentifier}/{gameId}",  userIdentifier,gameId)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            } catch (Exception e) {
                assert false;
            }
        }

    }

    @Nested
    class getUserCurrentGame {

        @Test
        void getUserCurrentGame_success() {
            //arrange
            when(currentGameServiceMock.getUserCurrentGame(userIdentifier)).thenReturn(sampleGameResponseDto());
            GameResponseDto expected = sampleGameResponseDto();

            //act//assert
            try {
                mockMvc.perform(get("/current/{userIdentifier}",userIdentifier).accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.userId").value(expected.getUserId()))
                        .andExpect(jsonPath("$.gameId").value(expected.getGameId()))
                        .andExpect(jsonPath("$.board").value(expected.getBoard()))
                        .andExpect(jsonPath("$.score").value(expected.getScore()));
            } catch (Exception e) {
                assert false;
            }

            verify(currentGameServiceMock, times(1)).getUserCurrentGame(userIdentifier);
        }

        @Test
        void getUserCurrentGame_fail() {
            //Arrange
            when(currentGameServiceMock.getUserCurrentGame(userIdentifier)).thenThrow(new ResourceNotFoundException("there is no current game"));

            //Act
            //Assert
            try {
                mockMvc.perform(get("/current/{userIdentifier}",userIdentifier)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
            } catch (Exception e) {
                assert false;
            }
        }

    }

    @Nested
    class moveSnakeInCurrentGame {

        @Test
        void moveSnakeInCurrentGame_success() {
            //Arrange
            GameResponseDto expected = sampleGameResponseDto();
            when(playValidatorMock.getChosenMoveEnum(any(MoveSnakeDto.class)))
                    .thenReturn(MoveSnakeEnum.UP);
            when(currentGameServiceMock.moveSnake(userIdentifier,MoveSnakeEnum.UP)).thenReturn(expected);

            //Act
            //Assert
            try {
                mockMvc.perform(post("/current/moveSnake")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestMoveSnakeDto())))
                        .andExpect(status().isOk())
                        .andExpect(content()
                                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.userId").value(expected.getUserId()))
                        .andExpect(jsonPath("$.gameId").value(expected.getGameId()))
                        .andExpect(jsonPath("$.score").value(expected.getScore()));

            } catch (Exception e) {
                assert false;
            }
        }

        @Test
        void moveSnakeInCurrentGame_fail() {
            //Arrange
            when(playValidatorMock.getChosenMoveEnum(any(MoveSnakeDto.class))).thenThrow(new ValidationException("baddd"));

            //Act
            //Assert
            try {
                mockMvc.perform(post("/current/moveSnake")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestMoveSnakeDto())))
                        .andExpect(status().isBadRequest())
                        .andExpect(content()
                                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

}