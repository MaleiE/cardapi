package com.malei.card.api.demo.validation;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {
       /* @ExceptionHandler(value = {UserNotFoundException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
   *//* @ResponseBody*//*
    public String handle1(UserNotFoundException e){
        String message = e.getMessage();
        return message;
    }*/

    @ExceptionHandler(value = {ConstraintViolationException.class })
    public String handle2(ConstraintViolationException e){
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations ) {
            strBuilder.append(violation.getMessage()).append("\n");
        }
        return strBuilder.toString();
    }
}
