package com.eden.orchid.presentations;

import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class FormsModel {

    @Getter @Setter
    private Map<String, Form> forms;

    @Inject
    public FormsModel() {
    }

    void initialize(Map<String, Form> forms) {
        this.forms = forms;
    }

}
