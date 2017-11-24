package com.malei.card.api.demo.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class SortParamsValidator implements ConstraintValidator<SortParamsConstraint, List<String>> {

    @Override
    public boolean isValid(List<String> strings, ConstraintValidatorContext constraintValidatorContext) {
        if(strings.size()==2){
            final String paramOne = strings.get(0);
            final String paramTwo = strings.get(1);
            if(paramTwo.equals("ask") || paramTwo.equals("desk")){
                if(paramOne.equals("id")||paramOne.equals("name")){
                    return true;
                }
            }
        }
        return false;
    }
}
