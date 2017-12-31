package com.eden.orchid.api.options;

import lombok.Value;

@Value
public class OptionsDescription {
    private String key;
    private Class optionType;
    private String description;
    private String defaultValue;
}
