package webSnake.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import webSnake.api.dto.*;
import webSnake.exception.ResourceNotFoundException;
import webSnake.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerUT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    String identifier = "hila";
    Long userId = 1L;
    String creationTime = LocalDateTime.now().toString();
    UserResponseDto userResponseDto = UserResponseDto.builder()
            .userId(userId).username("hila").creationTime(creationTime).lastPlayed(null).build();
    UserSumScoreDto userSumScoreDto = UserSumScoreDto.builder().sumScore(100L).build();
    UserRankDto userRankDto = UserRankDto.builder().rank(1L).build();
    CreateUserDto createUserDto = CreateUserDto.builder().username("hila").build();

    @Nested
    class CreateUser {

        @Test
        void createUser_success() throws Exception {
            // Arrange
            when(userService.createUser(any(CreateUserDto.class))).thenReturn(userResponseDto);

            // Act & Assert
            mockMvc.perform(post("/users/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createUserDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.userId").value(userId))
                    .andExpect(jsonPath("$.username").value("hila"))
                    .andExpect(jsonPath("$.creationTime").value(creationTime))
                    .andExpect(jsonPath("$.lastPlayed").isEmpty())
                    .andExpect(jsonPath("$.currentGameId").isEmpty());
        }

        @Test
        void createUser_serviceThrows_returns500() throws Exception {
            // Arrange
            when(userService.createUser(any(CreateUserDto.class)))
                    .thenThrow(new RuntimeException("error"));

            // Act & Assert
            mockMvc.perform(post("/users/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createUserDto)))
                    .andExpect(status().isInternalServerError());
        }

    }

    @Nested
    class GetUser {

        @Test
        void getUser_success() throws Exception {
            // Arrange
            when(userService.getUserByIdOrUsername(identifier)).thenReturn(userResponseDto);

            // Act & Assert
            mockMvc.perform(get("/users/{identifier}", identifier))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(userId))
                    .andExpect(jsonPath("$.username").value("hila"));
        }

        @Test
        void getUser_notFound_returns404() throws Exception {
            // Arrange
            when(userService.getUserByIdOrUsername(identifier))
                    .thenThrow(new ResourceNotFoundException("not found"));

            // Act & Assert
            mockMvc.perform(get("/users/{identifier}", identifier))
                    .andExpect(status().isNotFound());
        }

    }

    @Nested
    class GetUserScore {

        @Test
        void getUserScore_success() throws Exception {
            // Arrange
            when(userService.getScore(identifier)).thenReturn(userSumScoreDto);

            // Act & Assert
            mockMvc.perform(get("/users/score/{identifier}", identifier))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.sumScore").value(100L));
        }

    }

    @Nested
    class GetUserRank {

        @Test
        void getUserRank_success() throws Exception {
            // Arrange
            when(userService.getScoreRank(identifier)).thenReturn(userRankDto);

            // Act & Assert
            mockMvc.perform(get("/users/rank/{identifier}", identifier))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.rank").value(1));
        }

    }

    @Nested
    class DeleteUser {

        @Test
        void deleteUser_success() throws Exception {
            // Arrange
            doNothing().when(userService).deleteUser(identifier);

            // Act & Assert
            mockMvc.perform(delete("/users/{identifier}", identifier))
                    .andExpect(status().isNoContent());
        }

    }

    @Nested
    class GetAllUsers {

        @Test
        void getAllUsers_success() throws Exception {
            // Arrange
            when(userService.getAllUsers()).thenReturn(List.of(userResponseDto));

            // Act & Assert
            mockMvc.perform(get("/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].userId").value(userId))
                    .andExpect(jsonPath("$[0].username").value("hila"));
        }

    }

    @Nested
    class GetAllGames {

        @Test
        void getAllGames_success() throws Exception {
            // Arrange
            GameResponseDto gameResponseDto = GameResponseDto.builder()
                    .gameId(100L).score(10).build();
            when(userService.getAllUserGames(identifier)).thenReturn(List.of(gameResponseDto));

            // Act & Assert
            mockMvc.perform(get("/users/allGames/{identifier}", identifier))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].gameId").value(100L))
                    .andExpect(jsonPath("$[0].score").value(10));
        }

    }

}