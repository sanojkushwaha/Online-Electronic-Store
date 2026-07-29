package com.webapp.onlineelectronicstore.exceptions;

import com.webapp.onlineelectronicstore.dtos.response.ApiResponseMassage;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@RestControllerAdvice
@Builder
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //handler Resource not found Exception

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMassage> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        ApiResponseMassage apiResponseMassage =
                ApiResponseMassage.builder()
                        .message(e.getMessage())
                        .success(true)
                        .status(HttpStatus.NOT_FOUND)
                        .build();
        logger.info("Exception Handler Invoked !");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponseMassage);

    }

    //methodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> argumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        Map<String, Object> response = new HashMap<>();
        allErrors.stream().forEach(error -> {
            String message = error.getDefaultMessage();
            String field = ((FieldError) error).getField();
            response.put(field, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    //handle BAd api exception
    @ExceptionHandler(BadApiRequest.class)
    public ResponseEntity<ApiResponseMassage> handlerBadApiRequest(BadApiRequest e) {
        ApiResponseMassage apiResponseMassage =
                ApiResponseMassage.builder()
                        .message(e.getMessage())
                        .success(true)
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
        logger.info("Bad Api Request !");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseMassage);

    }
}