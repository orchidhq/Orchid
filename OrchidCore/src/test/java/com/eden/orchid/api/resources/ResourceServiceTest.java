package com.eden.orchid.api.resources;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.resources.resourceSource.PluginResourceSource;
import com.eden.orchid.api.resources.resourceSource.FileResourceSource;
import okhttp3.OkHttpClient;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@Test(groups={"services", "unit"})
public final class ResourceServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    private OrchidContext context;
    private ResourceService underTest;
    private ResourceServiceImpl service;
    private OkHttpClient client;

    private String resourcesDir;
    private Set<FileResourceSource> fileResourceSources;
    private FileResourceSource mockFileResourceSource;
    private Set<PluginResourceSource> pluginResourceSources;
    private PluginResourceSource mockPluginResourceSource;

    @BeforeMethod
    public void testSetup() {
        resourcesDir = "mockResourcesDir";
        fileResourceSources = new HashSet<>();
        mockFileResourceSource = mock(FileResourceSource.class);
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
