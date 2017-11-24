package com.malei.card.api.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="could not find card")
public class CardNotFoundException extends RuntimeException {
}
