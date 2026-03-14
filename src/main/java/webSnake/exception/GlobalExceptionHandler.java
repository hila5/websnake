package webSnake.exception;

import com.fasterxml.jackson.core.JsonParseException;
import jakarta.validation.ValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        Map<String, Object> body = Map.of("error", "Bad Request", "message", ex.getMessage());
        log.warn("handleValidationException raised", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> body = Map.of("error", "Resource not found", "message", ex.getMessage());
        log.warn("handleResourceNotFoundException raised", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseException(JsonParseException ex) {
        Map<String, Object> body = Map.of("error", "Bad Request", "message", ex.getMessage());
        log.warn("handleJsonParseException raised", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(GameMappingException.class)
    public ResponseEntity<Map<String, Object>> handleGameMappingException(GameMappingException ex) {
        Map<String, Object> body = Map.of("error", "Bad Request", "message", ex.getMessage());
        log.warn("handleGameMappingException raised", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}