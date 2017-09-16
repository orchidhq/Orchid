package com.eden.orchid.kss;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.JsonResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.kss.parser.StyleguideSection;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class KssPage extends OrchidPage {

    private KssPage parent;
    private List<KssPage> children;

    private StyleguideSection styleguideSection;

    private int[] sectionPath;

    private String stylesheet;

    public KssPage(OrchidContext context, StyleguideSection styleguideSection, String sectionBase, String sectionName) {
        this(new JsonResource(new JSONElement(new JSONObject()), new OrchidReference(context,
                "styleguide/"
                        + (!EdenUtils.isEmpty(sectionBase) ? sectionBase : "")
                        + OrchidUtils.normalizePath(String.join("/", sectionName.split("\\."))) + ".html")));
//        + "section-" + sectionName + ".html")));

        this.styleguideSection = styleguideSection;
        this.children = new ArrayList<>();

        String[] sectionPathPieces = sectionName.split("\\.");

        sectionPath = new int[sectionPathPieces.length];

        for (int i = 0; i < sectionPathPieces.length; i++) {
            sectionPath[i] = Integer.parseInt(sectionPathPieces[i]);
        }
    }

    public KssPage(OrchidResource resource) {
        super(resource, "styleguide");
    }

    @Override
    public String getTitle() {
        return "Section " +  styleguideSection.getStyleGuideReference() + " - " + styleguideSection.getName();
    }

    public String getStylesheet() {
        if(!EdenUtils.isEmpty(styleguideSection.getStylesheet())) {
            return styleguideSection.getStylesheet();
        }
        else if(!EdenUtils.isEmpty(stylesheet)) {
            return stylesheet;
        }

        return "";
    }

    public boolean hasStylesheet() {
        if(!EdenUtils.isEmpty(styleguideSection.getStylesheet())) {
            return true;
        }
        else if(!EdenUtils.isEmpty(stylesheet)) {
            return true;
        }

        return false;
    }
}
