package com.solace.maas.ep.event.management.agent.scanManager.rest;

import com.solace.maas.ep.common.model.EventErrorDTO;
import com.solace.maas.ep.event.management.agent.plugin.route.exceptions.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(ClientException.class)
    @ResponseBody
    public ResponseEntity<EventErrorDTO> handleRestException(ClientException restException) {
        EventErrorDTO eventErrorDTO = new EventErrorDTO(restException.getMessage());
        return new ResponseEntity<>(eventErrorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseBody
    public ResponseEntity<EventErrorDTO> handlePropertyReferenceException(PropertyReferenceException exception) {
        EventErrorDTO eventErrorDTO = new EventErrorDTO(exception.getMessage());
        return new ResponseEntity<>(eventErrorDTO, HttpStatus.BAD_REQUEST);
    }
}
