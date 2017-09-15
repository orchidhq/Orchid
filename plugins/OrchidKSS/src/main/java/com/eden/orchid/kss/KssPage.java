package com.eden.orchid.kss;

import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.JsonResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.kss.parser.Modifier;
import com.eden.orchid.kss.parser.StyleguideSection;
import com.eden.orchid.utilities.OrchidUtils;
import lombok.Getter;
import org.json.JSONObject;

@Getter
public class KssPage extends OrchidPage {

    private StyleguideSection styleguideSection;

    private int[] sectionPath;

    public KssPage(OrchidContext context, StyleguideSection styleguideSection, String sectionBase, String sectionName) {
        this(new JsonResource(new JSONElement(new JSONObject()), new OrchidReference(context,
                "styleguide/"
                        + (!EdenUtils.isEmpty(sectionBase) ? sectionBase : "")
                        + OrchidUtils.normalizePath(String.join("/", sectionName.split("\\."))) + "/index.html")));
        this.styleguideSection = styleguideSection;

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
        return styleguideSection.getDescription();
    }

    public String formatMarkup(Modifier modifier) {
        return styleguideSection.getMarkup().replaceAll("-modifierClass", modifier.className());
    }
}
