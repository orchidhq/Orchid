package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;

import javax.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;

public class HibernateValidator implements OptionsValidator {

    private final Validator validator;

    @Inject
    public HibernateValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public void validate(Object optionsHolder) throws Exception {
        Set<ConstraintViolation<Object>> violations = validator.validate(optionsHolder);

        if (!EdenUtils.isEmpty(violations)) {
            String msg = "\n" + violations
                    .stream()
                    .map(it -> "- " + it.getPropertyPath().toString() + ": " + it.getMessage())
                    .collect(Collectors.joining("\n"))
                    .trim();
            Clog.e("{}", msg);
            throw new Exception(msg);
        }
    }

}
