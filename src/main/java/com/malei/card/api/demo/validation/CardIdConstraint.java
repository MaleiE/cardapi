package com.malei.card.api.demo.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = CardIdValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CardIdConstraint {
    String message() default "Invalid card ID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
