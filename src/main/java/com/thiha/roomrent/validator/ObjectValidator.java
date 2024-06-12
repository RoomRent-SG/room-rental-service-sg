package com.thiha.roomrent.validator;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.thiha.roomrent.exceptions.InvalidObjectException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Component
public class ObjectValidator<T> {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    public void doVaildation(T objectToValidate){
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);
        if(!violations.isEmpty()){
            String errorMessage =  violations
                            .stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining("\n"));
            throw new InvalidObjectException(errorMessage);
        }
    }
}
