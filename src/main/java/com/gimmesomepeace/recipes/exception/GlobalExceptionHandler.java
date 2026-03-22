package com.gimmesomepeace.recipes.exception;

import com.gimmesomepeace.recipes.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        String detail = "%s с идентификатором %s не найден(а)".formatted(
                ex.getResourceType().getTitle(),
                ex.getResourceId()
        );
        ErrorResponse response = new ErrorResponse(
                ex.getResourceType().getType(),
                "Объект не найден",
                404,
                detail,
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(LoginAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleLoginAlreadyExists(
            LoginAlreadyExistsException exc,
            HttpServletRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
                "LOGIN_ALREADY_EXISTS",
                "Логин уже занят",
                409,
                "Пользователь с логином " + exc.getLogin() + " уже существует",
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
            UserNotFoundException exc,
            HttpServletRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
                "USER_NOT_FOUND",
                "Пользователь не найден",
                404,
                "Пользователь с логином " + exc.getLogin() + " не найден",
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(LoginFailedException.class)
    public ResponseEntity<ErrorResponse> handleLoginFailed(
            LoginFailedException exc,
            HttpServletRequest request
    ) {
        String detail = exc.getError().getDetail();

        ErrorResponse response = new ErrorResponse(
                "LOGIN_FAILED",
                "Ошибка авторизации",
                401,
                detail,
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exc,
            HttpServletRequest request
    ) {
        String detail = exc.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + ": " + err.getDefaultMessage())
            .collect(Collectors.joining("; "));


        ErrorResponse response = new ErrorResponse(
                "VALIDATION_ERROR",
                "Ошибка валидации",
                HttpStatus.BAD_REQUEST.value(),
                detail,
                request.getRequestURI(),
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception exc,
            HttpServletRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
                "INTERNAL_ERROR",
                "Внутрення ошибка сервера",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Что-то пошло не так",
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(
            ForbiddenException exc,
            HttpServletRequest request
    ) {
        String detail = exc.getMessage();
        ErrorResponse response = new ErrorResponse(
                "FORBIDDEN",
                "Отказано в доступе",
                403,
                detail,
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(RecipeValidationException.class)
    public ResponseEntity<ErrorResponse> handleRecipeValidation(
            RecipeValidationException exc,
            HttpServletRequest request
    ) {
        String detail = exc.getMessage();

        ErrorResponse response = new ErrorResponse(
                "VALIDATION_ERROR",
                "Ошибка валидации",
                400,
                detail,
                request.getRequestURI(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
