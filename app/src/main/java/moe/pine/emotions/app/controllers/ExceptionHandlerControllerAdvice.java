package moe.pine.emotions.app.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(Exception.class)
    public void handleException(
        Exception e,
        HttpServletResponse response
    ) throws IOException {
        log.error("An exception was occurred", e);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
