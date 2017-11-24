package com.malei.card.api.demo.validation;

import com.malei.card.api.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UserIdValidator  implements ConstraintValidator<UserIdConstraint, String> {

    @Autowired
    UserService userService;

    private static final Pattern id = Pattern.compile("\\d{1,5}");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(id.matcher(s).matches()){
            userService.getById(s);
            return true;
        } else {
            return false;
        }
    }
}
