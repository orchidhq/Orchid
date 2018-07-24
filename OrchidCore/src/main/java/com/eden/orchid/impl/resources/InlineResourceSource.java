package com.eden.orchid.impl.resources;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;
import lombok.Getter;

import javax.inject.Inject;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InlineResourceSource implements LocalResourceSource {

    private static final Pattern inlineFilenamePattern = Pattern.compile("^(inline:(.*?):)(.*)", Pattern.DOTALL);

    private final OrchidContext context;

    @Getter
    private final int priority;

    @Inject
    public InlineResourceSource(OrchidContext context) {
        this.context = context;
        this.priority = Integer.MAX_VALUE - 1;
    }

    @Override
    public OrchidResource getResourceEntry(String fileName) {
        Matcher m = inlineFilenamePattern.matcher(fileName);

        if(m.find()) {
            final String actualFileName = m.group(2);
            final String fileContent = m.group(3);
            return new StringResource(context, actualFileName, fileContent);
        }

        return null;
    }

    @Override
    public List<OrchidResource> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive) {
        return null;
    }

}
