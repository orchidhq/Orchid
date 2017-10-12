package com.eden.orchid.presentations;

import com.eden.common.util.EdenUtils;
import com.eden.orchid.api.resources.resource.OrchidResource;
import lombok.Getter;

public class Slide {

    @Getter
    private final OrchidResource slideContent;

    public Slide(OrchidResource slideContent) {
        this.slideContent = slideContent;
    }

    public String getContent() {
        if (slideContent != null && !EdenUtils.isEmpty(slideContent.getContent())) {
            String compiledContent = slideContent.getContent();

            if (slideContent.shouldPrecompile()) {
                compiledContent = slideContent.getContext().precompile(compiledContent);
            }

            return slideContent.getContext().compile(
                    slideContent.getReference().getExtension(),
                    compiledContent
            );
        }
        else {
            return "";
        }
    }

}
