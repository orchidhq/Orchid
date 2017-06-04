package com.eden.orchid.api.theme;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resourceSource.DefaultResourceSource;
import com.eden.orchid.api.theme.pages.OrchidPage;

import javax.inject.Inject;

public abstract class Theme extends DefaultResourceSource {

    @Inject
    public Theme(OrchidContext context) {
        super(context);
    }

    public String scripts() {
        String scripts = "<!-- start:inject scripts -->";

        for(OrchidPage script : context.getIndex().find("assets/js")) {
            scripts += "<script src=\"" + script.getLink() + "\"></script>";
        }

        scripts += "<!-- end:inject scripts -->";

        return scripts;
    }

    public String styles() {
        String styles = "<!-- start:inject styles -->";

        for(OrchidPage style : context.getIndex().find("assets/css")) {
            styles += "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + style.getLink() + "\"/>";
        }

        styles += "<!-- end:inject styles -->";

        return styles;
    }
}
