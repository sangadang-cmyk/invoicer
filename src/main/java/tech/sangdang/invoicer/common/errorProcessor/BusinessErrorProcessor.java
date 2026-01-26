package tech.sangdang.invoicer.common.errorProcessor;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.sangdang.invoicer.common.core.BusinessError;
import tech.sangdang.invoicer.common.core.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class BusinessErrorProcessor {
    @ExceptionHandler(BusinessError.class)
    public ResponseEntity<ErrorResponse> handleBusinessError(BusinessError ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getHttpStatus().value())
                .error(ex.getHttpStatus().name())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(error, ex.getHttpStatus());
    }
}
