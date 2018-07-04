package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.annotations.Validate;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
        if(optionsHolder.getClass().isAnnotationPresent(Validate.class)) {
            Set<ConstraintViolation<Object>> violations = validator.validate(optionsHolder);

            if (!EdenUtils.isEmpty(violations)) {
                String msg = optionsHolder.getClass().getSimpleName() + ": " +
                        violations
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(","));
                Clog.e("{}", msg);
                throw new Exception(msg);
            }
        }
    }

}
