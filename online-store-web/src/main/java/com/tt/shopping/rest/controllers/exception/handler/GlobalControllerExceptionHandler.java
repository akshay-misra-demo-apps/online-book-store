package com.tt.shopping.rest.controllers.exception.handler;

import com.tt.shopping.common.api.exceptions.BusinessValidationException;
import com.tt.shopping.common.api.exceptions.IncorrectRequestException;
import com.tt.shopping.common.api.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleNotFound(RuntimeException ex) {
        log.error("handleNotFound, exception: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectRequestException.class)
    public ResponseEntity<String> handleBadRequest(RuntimeException ex) {
        log.error("handleBadRequest, exception: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ResponseEntity<String> handleValidationFailure(RuntimeException ex) {
        log.error("handleValidationFailure, exception: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllErrors(RuntimeException ex) {
        log.error("handleAllErrors, exception: ", ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
