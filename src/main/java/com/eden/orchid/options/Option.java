package com.eden.orchid.options;


import com.eden.common.json.JSONElement;

public interface Option {
    String getFlag();
    String getDescription();

    JSONElement parseOption(String[] options);
    JSONElement getDefaultValue();
    int priority();

    boolean required();

    int optionLength();
}
