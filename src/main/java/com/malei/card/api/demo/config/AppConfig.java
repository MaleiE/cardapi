package com.malei.card.api.demo.config;

import com.malei.card.api.demo.exception.UserNotFoundException;
import org.hibernate.validator.HibernateValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Map;

@Configuration
public class AppConfig {
   /* @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }*/

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


/*    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest requestAttributes, boolean includeStackTrace) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
                Throwable error = getError(requestAttributes);
                System.out.println("DefaultErrorAttributes");
                System.out.println(error.getMessage());

                if (error instanceof UserNotFoundException) {
                    System.out.println("DefaultErrorAttributes IIF");

                    UserNotFoundException myException = (UserNotFoundException) error;

                    errorAttributes.put("errorCode", "400" );
                    errorAttributes.put("message", myException.getMessage());
                    errorAttributes.put("status", 400);

                    HttpStatus correspondentStatus = HttpStatus.valueOf(400);
                    errorAttributes.put("error", correspondentStatus.getReasonPhrase());
                    requestAttributes.setAttribute("javax.servlet.error.status_code", 400, 0);

                }

                if (error instanceof ConstraintViolationException) {
                    System.out.println("DefaultErrorAttributes IIF");
                    ConstraintViolationException exception = (ConstraintViolationException) error;
                    errorAttributes.put("errorCode", "400" );
                    errorAttributes.put("message", exception.getMessage());
                    errorAttributes.put("status", 400);
                    HttpStatus correspondentStatus = HttpStatus.valueOf(400);
                    errorAttributes.put("error", correspondentStatus.getReasonPhrase());
                    requestAttributes.setAttribute("javax.servlet.error.status_code", 400, 0);

                }
                return errorAttributes;
            }

        };
    }*/


}
