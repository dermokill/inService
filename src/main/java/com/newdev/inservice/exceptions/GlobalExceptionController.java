package com.newdev.inservice.exceptions;


import com.newdev.inservice.responseDtos.JsonResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionController {


    @ExceptionHandler(ConstraintViolationException.class) // For validation errors on DTOs RequestBody
    public ResponseEntity<JsonResponse> handleConstraintViolation(ConstraintViolationException ex) {
        System.out.println(ex.getMessage());
        return ResponseEntity.badRequest().body(
                new JsonResponse(ex.getConstraintViolations()
                        .stream()
                        .map(cv -> cv.getMessage())
                        .collect(Collectors.joining(" ; "))
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class) //Applies to individual @RequestParam, @PathVariable, etc
    public ResponseEntity<JsonResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        System.out.println(ex.getMessage());
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(" ; "));
        return ResponseEntity.badRequest().body(new JsonResponse(errorMessage));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<JsonResponse> handleUnauthorizedException(UnauthorizedException ex) {
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(new JsonResponse(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<JsonResponse> handleNotFoundException(ResourceNotFoundException ex) {
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(new JsonResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<JsonResponse> handleConflictException(ConflictException ex) {
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(new JsonResponse(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<JsonResponse> handleBadRequestException(BadRequestException ex) {
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(new JsonResponse(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JsonResponse> handleGenericException(Exception ex) {
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(new JsonResponse("An unexpected error occurred: "+ ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
