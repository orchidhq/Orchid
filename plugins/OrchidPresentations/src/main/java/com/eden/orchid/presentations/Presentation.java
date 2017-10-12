package com.eden.orchid.presentations;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Presentation {

    @Getter private final String key;
    @Getter private final List<Slide> slides;

    @Getter @Setter private String title;

    public Presentation(String key, List<Slide> slides) {
        this.key = key;
        this.slides = slides;
    }

}
