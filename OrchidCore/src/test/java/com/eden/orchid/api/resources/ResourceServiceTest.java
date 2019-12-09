package com.eden.orchid.api.resources;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.resources.resourcesource.LocalResourceSource;
import com.eden.orchid.api.resources.resourcesource.PluginResourceSource;
import com.eden.orchid.testhelpers.OrchidUnitTest;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public final class ResourceServiceTest implements OrchidUnitTest {

    private OrchidContext context;
    private ResourceService underTest;
    private ResourceServiceImpl service;
    private OkHttpClient client;

    private String resourcesDir;
    private Set<LocalResourceSource> fileResourceSources;
    private LocalResourceSource mockFileResourceSource;
    private Set<PluginResourceSource> pluginResourceSources;
    private PluginResourceSource mockPluginResourceSource;

    @BeforeEach
    public void setUp() {
        resourcesDir = "mockResourcesDir";
        fileResourceSources = new HashSet<>();
        mockFileResourceSource = mock(LocalResourceSource.class);
        fileResourceSources.add(mockFileResourceSource);

        pluginResourceSources = new HashSet<>();
        mockPluginResourceSource = mock(PluginResourceSource.class);
        pluginResourceSources.add(mockPluginResourceSource);

        // test the service directly
        context = mock(OrchidContext.class);
        client = mock(OkHttpClient.class);
        service = new ResourceServiceImpl(resourcesDir, fileResourceSources, pluginResourceSources, client);
        service.initialize(context);

        // test that the public implementation is identical to the real implementation
        underTest = new ResourceService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

}
