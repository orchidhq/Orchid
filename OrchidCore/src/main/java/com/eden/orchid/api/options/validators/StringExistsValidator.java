package com.eden.orchid.api.options.validators;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.options.OptionValidator;
import com.eden.orchid.api.options.ValidationResult;

import javax.inject.Inject;

public final class StringExistsValidator extends OptionValidator<String> {

    @Inject
    public StringExistsValidator() {
        super("str_exists", 10);
    }

    @Override
    public boolean acceptsClass(Class clazz) {
        return clazz.equals(String.class);
    }

    @Override
    public ValidationResult validate(String key, String value, String[] config) {
        return new ValidationResult(!EdenUtils.isEmpty(value), Clog.format("{} must be a string and cannot be empty.", key));
    }

}
