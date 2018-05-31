package com.gm.record.exception.advice;


import com.gm.record.exception.RecordNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * AOP approach to handling exception. This advice kicks in whenever there is an exception while
 * executing RecordApi Service API(s)
 */
@Order(MAX_VALUE)
@ControllerAdvice
@Slf4j
public class RecordServiceExceptionAdvice {

    /**
     * {@link RecordNotFoundException} handler
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {RecordNotFoundException.class})
    public ResponseEntity<RecordAdviceObject> recordNotFound(RecordNotFoundException ex) {
        log.debug(format("No Record found: ", ex.getMessage()));
        RecordAdviceObject error = new RecordAdviceObject(ex.getMessage());
        return new ResponseEntity<>(error, NOT_FOUND);
    }

    /**
     * Generic exception handling
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<RecordAdviceObject> anyException(Exception ex) {
        String message = ex.getMessage();
        if (message == null || message.isEmpty()) {
            if (ex.getStackTrace().length > 0) {
                StackTraceElement ste = ex.getStackTrace()[0];
                message = format("Exception %s found in %s (method %s) at line number %s", ex.getClass().getName(), ste.getClassName(), ste.getMethodName(), ste.getLineNumber());
            }
        }
        log.debug(format("No Record found: ", ex.getMessage()));
        RecordAdviceObject error = new RecordAdviceObject(message);
        return new ResponseEntity<>(error, BAD_REQUEST);
    }
}
