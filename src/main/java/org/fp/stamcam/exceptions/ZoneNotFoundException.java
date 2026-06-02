package org.fp.stamcam.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ZoneNotFoundException extends RuntimeException {

    public ZoneNotFoundException(String id) {
        super("Zone not found with ID: " + id);
    }
}