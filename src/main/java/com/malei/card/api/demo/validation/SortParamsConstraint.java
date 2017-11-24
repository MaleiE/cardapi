package com.malei.card.api.demo.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = SortParamsValidator.class)
@Target( {ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SortParamsConstraint {
    String message() default "Invalid sort params";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
