package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.common.model.EventErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class RestErrorHandler {

    @ExceptionHandler({RestException.class})
    @ResponseBody
    public ResponseEntity<EventErrorDTO> handleRestException(RestException restException) {
        EventErrorDTO eventErrorDTO = new EventErrorDTO(restException.getMessage());
        return new ResponseEntity<>(eventErrorDTO, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public static class RestException extends RuntimeException {
        public RestException(String message) {
            super(message);
        }

        public RestException(String message, Exception cause) {
            super(message, cause);
        }
    }
}
