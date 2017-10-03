package com.eden.orchid.javadoc.pages;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.FreeableResource;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Tag;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ClassDocResource extends FreeableResource {

    private final ClassDoc classDoc;

    public ClassDocResource(OrchidContext context, ClassDoc classDoc) {
        super(new OrchidReference(context, classDoc.qualifiedName().replaceAll("\\.", "/") + ".html"));
        this.classDoc = classDoc;
        reference.setExtension("md");
    }

    @Override
    protected void loadContent() {
        if (rawContent == null) {
            if (classDoc != null) {
                rawContent = Arrays
                        .stream(classDoc.inlineTags())
                        .map(Tag::text)
                        .collect(Collectors.joining(" "));
            }
        }

        super.loadContent();
    }

}
