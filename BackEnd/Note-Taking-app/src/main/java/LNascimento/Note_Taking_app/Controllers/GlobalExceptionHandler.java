package LNascimento.Note_Taking_app.Controllers;

import LNascimento.Note_Taking_app.DTOs.ExceptionDTOs.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<StandardErrorDTO> handleEntityExists(EntityExistsException e, HttpServletRequest request){
        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                e.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handleValidationExceptions(MethodArgumentNotValidException e, HttpServletRequest request) {

        List<FieldMessageDTO> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(err -> new FieldMessageDTO(err.getField(), err.getDefaultMessage()))
                .collect(Collectors.toList());

        ValidationErrorDTO error = new ValidationErrorDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                "Data validation error",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardErrorDTO> handleMessageNotReadable(HttpMessageNotReadableException e, HttpServletRequest request) {
        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Malformed Request",
                "the json body is missing or malformed",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<StandardErrorDTO> handleMissingParams(MissingServletRequestParameterException e, HttpServletRequest request) {
        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Missing Parameter",
                "The url parameter '" + e.getParameterName() + "' is missing.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<StandardErrorDTO> handleBadCredentials(Exception e, HttpServletRequest request) {

        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(), // HTTP 401
                "Authentication Failed",
                "Email or password incorrect.", // Mensagem genérica e segura!
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<StandardErrorDTO> handleTokenExpired(ExpiredJwtException e, HttpServletRequest request) {
        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(), // 401
                "Token Expired",
                "Access token expired, please log in again.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 2. Tratamento para Token Inválido (Malformado, assinatura errada, etc)
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<StandardErrorDTO> handleInvalidToken(JwtException e, HttpServletRequest request) {
        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(), // 401
                "Invalid Token",
                "Invalid token.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<StandardErrorDTO> handleNoSuchElement(NoSuchElementException e, HttpServletRequest request) {
        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(), // 401
                "Not Found",
                "Element not found.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardErrorDTO> handleEntityNotFound(EntityNotFoundException e, HttpServletRequest request) {
        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(), // 401
                "Not Found",
                "Entity not found.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<StandardErrorDTO> handleNoResourceFound(NoResourceFoundException e, HttpServletRequest request) {
        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(), // 401
                "Not Found",
                "invalid route.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<StandardErrorDTO> invalidDataApiUsage(InvalidDataAccessApiUsageException e, HttpServletRequest request) {
        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(), // 401
                "Bad Request",
                "invalid sorting method.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
