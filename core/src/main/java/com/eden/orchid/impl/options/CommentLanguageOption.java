package com.eden.orchid.impl.options;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.options.OrchidOption;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CommentLanguageOption extends OrchidOption {

    @Inject
    public CommentLanguageOption() {
        this.priority = 600;
    }

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
    public int optionLength() {
        return 2;
    }
}
