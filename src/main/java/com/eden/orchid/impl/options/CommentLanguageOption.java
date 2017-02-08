package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.options.OrchidOption;
import com.eden.orchid.api.registration.AutoRegister;

@AutoRegister
public class CommentLanguageOption implements OrchidOption {

    @Override
    public String getFlag() {
        return "commentExt";
    }

    @Override
    public String getDescription() {
        return "the extension of the language used to parse Javadoc comments. Defaults to 'md' for Markdown.";
    }

    @Override
    public JSONElement parseOption(String[] options) {
        return new JSONElement(options[1]);
    }

    @Override
    public JSONElement getDefaultValue() {
        return new JSONElement("md");
    }

    @Override
    public int priority() {
        return 600;
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
