package com.malei.card.api.demo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IdValidator.class)
@Target({ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IdConstraint {
    String message() default "Invalid ID";
    String entity();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
