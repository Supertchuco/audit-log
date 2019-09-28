package com.auditlog.auditlog.exception;

import com.auditlog.auditlog.enumerator.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class ExceptionResourceHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(final Exception exception, final WebRequest request) {
        log.error("Unexpected error", exception);
        final ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ErrorMessages.UNEXPECTED_ERROR.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OperationPerformedNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleOperationPerformedNotFoundException(final WebRequest request) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ErrorMessages.OPERATION_PERFORMED_NOT_FOUND.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDateOperationPerformedException.class)
    public final ResponseEntity<ExceptionResponse> handleInvaldDateOperationPerformedException(final WebRequest request) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ErrorMessages.INVALID_DATE_FORMAT.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OperationPerformedAlreadyExistOnDatabaseException.class)
    public final ResponseEntity<ExceptionResponse> handleOperationPerformedAlreadyExistOnDatabaseException(final WebRequest request) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                ErrorMessages.OPERATION_PERFORMED_ALREADY_EXIST_ON_DATABASE.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(final WebRequest request) {
        final ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ErrorMessages.INVALID_PAYLOAD.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
