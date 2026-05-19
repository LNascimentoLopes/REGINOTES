package LNascimento.Note_Taking_app.Controllers;

import LNascimento.Note_Taking_app.DTOs.ExceptionDTOs.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;
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

        // 1. Extrai a lista de erros do Spring e converte para o nosso DTO limpo
        List<FieldMessageDTO> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(err -> new FieldMessageDTO(err.getField(), err.getDefaultMessage()))
                .collect(Collectors.toList());

        // 2. Monta o corpo da resposta
        ValidationErrorDTO error = new ValidationErrorDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(), // Devolve HTTP 400
                "Validation Error",
                "Erro na validação dos dados enviados",
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
                "O corpo da requisição (JSON) está ausente ou mal formatado.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 2. Trata falta de parâmetros na URL (?param=valor)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<StandardErrorDTO> handleMissingParams(MissingServletRequestParameterException e, HttpServletRequest request) {
        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Missing Parameter",
                "O parâmetro de URL obrigatório '" + e.getParameterName() + "' está ausente.",
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
                "E-mail ou senha incorretos.", // Mensagem genérica e segura!
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
                "O seu token de acesso expirou. Por favor, faça login novamente.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 2. Tratamento para Token Inválido (Malformado, assinatura errada, etc)
    @ExceptionHandler()
    public ResponseEntity<StandardErrorDTO> handleInvalidToken(JwtException e, HttpServletRequest request) {
        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(), // 401
                "Invalid Token",
                "O token fornecido é inválido ou foi adulterado.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

}
