package com.malei.card.api.demo.config;

import org.hibernate.validator.HibernateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
public class AppConfig {

    @Bean
    public Validator validator(final AutowireCapableBeanFactory autowireCapableBeanFactory) {

        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .constraintValidatorFactory(new SpringConstraintValidatorFactory(autowireCapableBeanFactory))
                .buildValidatorFactory();

        return validatorFactory.getValidator();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
