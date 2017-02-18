package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.options.OrchidOption;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ResourcesOption extends OrchidOption {

    private String[] dataExtensions = new String[] {"yml", "yaml", "json"};

    @Inject
    public ResourcesOption() {
        this.priority = 1000;
    }

    @Override
    public String getFlag() {
        return "resourcesDir";
    }

    @Override
    public String getDescription() {
        return "the directory containing your content files";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        return new JSONElement(options[1]);
    }

    @Override
    public JSONElement getDefaultValue() {
        return null;
    }

    @Override
    public int optionLength() {
        return 2;
    }

    @Override
    public boolean isRequired() {
        return true;
    }
}
