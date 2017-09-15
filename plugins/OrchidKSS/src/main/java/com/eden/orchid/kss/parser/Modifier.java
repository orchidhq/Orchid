package com.eden.orchid.kss.parser;

public class Modifier {

    public String name;
    public String description;

    public Modifier(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String className() {
        return name.replaceAll("\\.", "").replaceAll(":", " pseudo-class-").trim();
    }
    
}
