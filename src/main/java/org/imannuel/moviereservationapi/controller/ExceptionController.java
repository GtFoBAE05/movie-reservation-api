package org.imannuel.moviereservationapi.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.imannuel.moviereservationapi.dto.mapper.template.ApiMapper;
import org.imannuel.moviereservationapi.dto.response.template.ApiTemplateResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> handlingResponseStatusException(ResponseStatusException e) {
        return ApiMapper.basicMapper(
                HttpStatus.valueOf(e.getStatusCode().value()), e.getReason(), null
        );
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<?> handlingDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = "";
        HttpStatus status = HttpStatus.CONFLICT;

        if (e.getCause() != null) {
            String causeMessage = e.getCause().getMessage();
            if (causeMessage.contains("duplicate key value")) {
                message = "Data already exist.";
            } else if (causeMessage.contains("cannot be null")) {
                message = "Data cannot be null.";
                status = HttpStatus.BAD_REQUEST;
            } else if (causeMessage.contains("foreign key constraint")) {
                message = "Data cannot be deleted because it is used by other data.";
                status = HttpStatus.BAD_REQUEST;
            } else if (causeMessage.contains("Duplicate entry")) {
                message = "Data already exist.";
            } else {
                message = "Unexpected error occurred";
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        return ApiMapper.basicMapper(status, message, null);
    }

    @ExceptionHandler({InvalidDataAccessResourceUsageException.class})
    public ResponseEntity<?> handlingInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException e) {
        return ApiMapper.basicMapper(
                HttpStatus.BAD_REQUEST, "Invalid data to sort by", null
        );
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handlingConstraintViolationException(ConstraintViolationException e) {
        return ApiMapper.basicMapper(
                HttpStatus.BAD_REQUEST, e.getMessage(), null
        );
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<?> handlingMissingServletRequestParameterExceptionn(MissingServletRequestParameterException e) {
        return ApiMapper.basicMapper(
                HttpStatus.BAD_REQUEST, e.getMessage(), null
        );
    }

}
