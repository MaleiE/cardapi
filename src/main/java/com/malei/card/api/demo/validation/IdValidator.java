package com.malei.card.api.demo.validation;

import com.malei.card.api.demo.service.CardService;
import com.malei.card.api.demo.service.PurchaseService;
import com.malei.card.api.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IdValidator implements ConstraintValidator<IdConstraint, String> {

    private static final Pattern ID = Pattern.compile("\\d{1,5}");

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseService purchaseService;

    private String entity;

    @Override
    public void initialize(IdConstraint constraintAnnotation) {
        entity = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
            switch (entity){
                case "user":
                    if(ID.matcher(s).matches()){
                        userService.getById(s);
                        return true;
                    } else
                        return false;
                case "card":
                    if(ID.matcher(s).matches()){
                        cardService.getById(s);
                        return true;
                    } else
                        return false;
                case "purchase":
                    if(ID.matcher(s).matches()){
                        purchaseService.getPurchaseById(s);
                        return true;
                    } else
                        return false;
                default:
                    throw new IllegalArgumentException("The annotation parameter \"entity\" does not correspond to entities");

            }
    }


}
