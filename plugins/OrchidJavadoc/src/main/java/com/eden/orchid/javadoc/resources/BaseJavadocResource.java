package com.eden.orchid.javadoc.resources;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.FreeableResource;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BaseJavadocResource extends FreeableResource {

    protected Doc doc;

    public BaseJavadocResource(OrchidContext context, String qualifiedName, Doc doc) {
        super(new OrchidReference(context, qualifiedName.replaceAll("\\.", "/") + ".html"));
        this.doc = doc;
        reference.setExtension("md");
    }

    @Override
    protected void loadContent() {
        if (rawContent == null) {
            if (doc != null) {
                rawContent = Arrays
                        .stream(doc.inlineTags())
                        .map(Tag::text)
                        .collect(Collectors.joining(" "));
                content = rawContent;

                Map<String, List<String>> tagMap = new HashMap<>();
                for (Tag tag : doc.tags()) {
                    String key = tag.name().substring(1);
                    if (!tagMap.containsKey(key)) {
                        tagMap.put(key, new ArrayList<>());
                    }
                    tagMap.get(key).add(tag.text());
                }

                JSONObject data = new JSONObject();
                for (Map.Entry<String, List<String>> entry : tagMap.entrySet()) {
                    if (entry.getValue().size() == 1) {
                        data.put(entry.getKey(), entry.getValue().get(0));
                    }
                    else {
                        data.put(entry.getKey(), entry.getValue());
                    }
                }

                this.embeddedData = new JSONElement(data);
            }
        }
    }

}
