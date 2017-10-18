package com.eden.orchid.api.options;

import com.eden.orchid.api.registration.Prioritized;
import lombok.Getter;

public abstract class OptionValidator<T> extends Prioritized {

    @Getter
    private final String key;

    public OptionValidator(String key, int priority) {
        super(priority);
        this.key = key;
    }

    public abstract boolean acceptsClass(Class clazz);

    public abstract ValidationResult validate(String key, T value, String[] config);

}
