package com.eden.orchid.testhelpers;

import com.eden.common.json.JSONElement;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.resource.JsonResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;
import com.eden.orchid.api.resources.resourceSource.OrchidResourceSource;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.google.inject.Provider;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestConfigResourceSource implements OrchidResourceSource, LocalResourceSource {

    private final Provider<OrchidContext> context;
    private final Map<String, Object> mockConfig;

    @Inject
    public TestConfigResourceSource(Provider<OrchidContext> context, Map<String, Object> mockConfig) {
        this.context = context;
        this.mockConfig = mockConfig;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE - 1;
    }

    @Override
    public OrchidResource getResourceEntry(String fileName) {
        if(fileName.equals("config.yml")) {
            JSONElement el = new JSONElement(new JSONObject(mockConfig));
            OrchidReference ref = new OrchidReference(context.get(), "config.yml");

            return new JsonResource(el, ref);
        }

        return null;
    }

    @Override
    public List<OrchidResource> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive) {
        return new ArrayList<>();
    }

    public OrchidModule toModule() {
        return new OrchidModule() {
            @Override
            protected void configure() {
                addToSet(LocalResourceSource.class, TestConfigResourceSource.this);
            }
        };
    }


}
