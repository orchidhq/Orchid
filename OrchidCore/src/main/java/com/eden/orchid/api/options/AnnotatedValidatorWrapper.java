package com.eden.orchid.api.options;

import com.eden.orchid.api.options.annotations.Validate;

import javax.inject.Inject;

public class AnnotatedValidatorWrapper implements OptionsValidator {

    private final OptionsValidator validator;

    @Inject
    public AnnotatedValidatorWrapper(OptionsValidator validator) {
        this.validator = validator;
    }

    @Override
    public void validate(Object optionsHolder) throws Exception {
        if(optionsHolder.getClass().isAnnotationPresent(Validate.class)) {
            validator.validate(optionsHolder);
        }
    }

}
