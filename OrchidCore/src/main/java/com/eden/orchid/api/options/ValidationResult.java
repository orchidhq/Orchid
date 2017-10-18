package com.eden.orchid.api.options;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ValidationResult {

    private boolean isValid;
    private String message;

}
