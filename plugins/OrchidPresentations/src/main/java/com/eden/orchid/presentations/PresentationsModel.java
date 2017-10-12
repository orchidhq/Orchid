package com.eden.orchid.presentations;

import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class PresentationsModel {

    @Getter @Setter
    private Map<String, Presentation> presentations;

    @Inject
    public PresentationsModel() {
    }

    void initialize(Map<String, Presentation> presentations) {
        this.presentations = presentations;
    }

}
