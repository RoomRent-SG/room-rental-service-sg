package com.thiha.roomrent.validator;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.thiha.roomrent.exceptions.InvalidObjectException;
import jakarta.validation.ConstraintViolation;

@Component
public class ObjectValidator<T> {

    private final LocalValidatorFactoryBean validator;

    public ObjectValidator(LocalValidatorFactoryBean validator){
        this.validator = validator;
    }
    
    public void doVaildation(T objectToValidate){
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);
        System.out.println("Empty Violation.."+violations.isEmpty());
        if(!violations.isEmpty()){
            String errorMessage =  violations
                            .stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining("\n"));
            throw new InvalidObjectException(errorMessage);
        }
    }
}
