package com.eden.orchid.testhelpers;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.registration.OrchidModule;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.resources.resourceSource.LocalResourceSource;
import com.eden.orchid.api.resources.resourceSource.OrchidResourceSource;
import com.google.inject.Provider;
import kotlin.Pair;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestResourceSource implements OrchidResourceSource, LocalResourceSource {

    private final Provider<OrchidContext> context;
    private final Map<String, Pair<String, Map<String, Object>>> mockResources;

    @Inject
    public TestResourceSource(Provider<OrchidContext> context, Map<String, Pair<String, Map<String, Object>>> mockResources) {
        this.context = context;
        this.mockResources = mockResources;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE - 2;
    }

    @Override
    public OrchidResource getResourceEntry(String fileName) {
        if(mockResources.containsKey(fileName)) {
            return new StringResource(context.get(), fileName, mockResources.get(fileName).getFirst(), mockResources.get(fileName).getSecond());
        }

        return null;
    }

    @Override
    public List<OrchidResource> getResourceEntries(String dirName, String[] fileExtensions, boolean recursive) {
        return mockResources
                .entrySet()
                .stream()
                .filter(it -> it.getKey().startsWith(dirName))
                .map(it -> new StringResource(context.get(), it.getKey(), it.getValue().getFirst(), it.getValue().getSecond()))
                .collect(Collectors.toList());
    }

    public OrchidModule toModule() {
        return new OrchidModule() {
            @Override
            protected void configure() {
                addToSet(LocalResourceSource.class, TestResourceSource.this);
            }
        };
    }
}
