package com.malei.card.api.demo.validation;

import com.malei.card.api.demo.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CardIdValidator implements ConstraintValidator<CardIdConstraint, String> {
    @Autowired
    CardService cardService;

    private static final Pattern id = Pattern.compile("\\d{1,5}");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(id.matcher(s).matches()){
            cardService.getById(s);
            return true;
        } else {
            return false;
        }
    }
}
