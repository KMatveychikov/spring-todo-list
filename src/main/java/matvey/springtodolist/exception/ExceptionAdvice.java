package matvey.springtodolist.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        String message = String.format("%s: %s", LocalDateTime.now(), e.getMessage());
        ExceptionResponse response = new ExceptionResponse(message);
        log.error(String.format("%s: %s", LocalDateTime.now(), e.getMessage()));
        return  new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
