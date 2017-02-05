package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.options.Option;
import com.eden.orchid.utilities.AutoRegister;

@AutoRegister
public class EnvironmentOption implements Option {

    @Override
    public String getFlag() {
        return "environment";
    }

    @Override
    public String getDescription() {
        return "the development environment. Reads 'config-<environment>.yml' and may alter the behavior of registered components";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        return new JSONElement(options[1]);
    }

    @Override
    public JSONElement getDefaultValue() {
        return new JSONElement("dev");
    }

    @Override
    public int priority() {
        return 80;
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public int optionLength() {
        return 2;
    }
}
