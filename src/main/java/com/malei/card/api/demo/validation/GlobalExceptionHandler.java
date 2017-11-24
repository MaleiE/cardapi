package com.malei.card.api.demo.validation;

import com.malei.card.api.demo.exception.UserNotFoundException;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
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
        System.out.println(e.getMessage()+" GRRRRRRRR");
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
