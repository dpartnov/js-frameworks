package com.etnetera.hr.exception;

import com.etnetera.hr.data.dto.ErrorMessageDto;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Custom exception handler for correctly exception processing.
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity handleValidationException(Exception ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessageDto(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity handleConstraintViolationException(Exception ex, WebRequest request) {
        ConstraintViolationException constEx = (ConstraintViolationException) ex;
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(constEx.getConstraintViolations()
                        .stream().map(e -> new ErrorMessageDto(e.getMessage())).collect(Collectors.toList()));
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity handleAllExceptions(Exception ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getStackTrace());
    }
}
