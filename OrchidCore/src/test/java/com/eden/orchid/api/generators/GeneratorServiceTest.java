package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.indexing.OrchidRootExternalIndex;
import com.eden.orchid.api.indexing.OrchidRootInternalIndex;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.resources.resource.FreeableResource;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

public final class GeneratorServiceTest {

    private OrchidContext context;
    private OptionsExtractor extractor;
    private Theme theme;
    private BuildMetrics buildMetrics;

    private GeneratorService underTest;
    private GeneratorServiceImpl service;

    private OrchidRootInternalIndex internalIndex;
    private OrchidRootExternalIndex externalIndex;

    private Set<OrchidGenerator> generators;

    private MockGenerator generator1;
    private List<OrchidPage> pages1;
    private OrchidPage mockPage1;
    private OrchidReference mockPage1Reference;

    private MockGenerator generator2;
    private List<OrchidPage> pages2;
    private FreeableResource mockFreeableResource;
    private OrchidPage mockPage2;
    private OrchidReference mockPage2Reference;

    private MockGenerator generator3;
    private List<OrchidPage> pages3;

    @BeforeEach
    public void testSetup() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
        context = mock(OrchidContext.class);
        extractor = mock(OptionsExtractor.class);
        theme = mock(Theme.class);
        buildMetrics = mock(BuildMetrics.class);
        when(context.resolve(OptionsExtractor.class)).thenReturn(extractor);
        when(context.findTheme(any())).thenReturn(theme);

        when(theme.isHasRenderedAssets()).thenReturn(true);

        internalIndex = new OrchidRootInternalIndex();
        externalIndex = new OrchidRootExternalIndex();

        when(context.getInternalIndex()).thenReturn(internalIndex);
        when(context.getExternalIndex()).thenReturn(externalIndex);

        generators = new HashSet<>();

        pages1 = new ArrayList<>();
        mockPage1 = mock(OrchidPage.class);
        mockPage1Reference = new OrchidReference(context, "page1.html");
        when(mockPage1.getReference()).thenReturn(mockPage1Reference);
        pages1.add(mockPage1);
        generator1 = new MockGenerator(context, "gen1", 100, pages1);
        generator1 = spy(generator1);
        generators.add(generator1);

        pages2 = new ArrayList<>();
        mockPage2 = mock(OrchidPage.class);
        mockFreeableResource = mock(FreeableResource.class);
        mockPage2Reference = new OrchidReference(context, "page2.html");
        when(mockPage2.getResource()).thenReturn(mockFreeableResource);
        when(mockPage2.getReference()).thenReturn(mockPage2Reference);
        pages2.add(mockPage2);
        generator2 = new MockGenerator(context, "gen2", 150, pages2);
        generator2 = spy(generator2);
        generators.add(generator2);

        pages3 = null;
        generator3 = new MockGenerator(context, "gen3", 200, pages3);
        generator3 = spy(generator3);
        generators.add(generator3);

        when(context.getGeneratorPages(generator1.getKey())).thenReturn(pages1);
        when(context.getGeneratorPages(generator2.getKey())).thenReturn(pages2);
        when(context.getGeneratorPages(generator3.getKey())).thenReturn(null);

        // test the service directly
        service = new GeneratorServiceImpl(generators, buildMetrics);
        service.initialize(context);

        // test that the default implementation is identical to the real implementation
        underTest = new GeneratorService() {
            public void initialize(OrchidContext context) { }

            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

    @Test
    public void testSetupCorrectly() throws Throwable {
        underTest.startIndexing();
        underTest.startGeneration();

        verify(generator1).extractOptions(any(), any());
        verify(generator1).startIndexing();
        assertThat(generator1.mockPages.size(), is(1));
        assertThat(generator1.mockPages.size(), is(1));

        verify(generator2).extractOptions(any(), any());
        verify(generator2).startIndexing();
        assertThat(generator2.mockPages.size(), is(1));

        verify(generator3).extractOptions(any(), any());
        verify(generator3).startIndexing();
        assertThat(generator3.mockPages, is(nullValue()));
    }

    @Test
    public void testFilteringGenerators() throws Throwable {
        service.startIndexing();
        List<OrchidGenerator> generators;

        generators = service.getFilteredGenerators(false).collect(Collectors.toList());
        assertThat(generators, containsInAnyOrder(generator1, generator2, generator3));

        service.setDisabled(new String[]{"gen1"});

        generators = service.getFilteredGenerators(false).collect(Collectors.toList());
        assertThat(generators, containsInAnyOrder(generator2, generator3));

        service.setDisabled(null);

        generators = service.getFilteredGenerators(false).collect(Collectors.toList());
        assertThat(generators, containsInAnyOrder(generator1, generator2, generator3));

        service.setEnabled(new String[]{"gen1"});

        generators = service.getFilteredGenerators(false).collect(Collectors.toList());
        assertThat(generators, containsInAnyOrder(generator1));

        service.setDisabled(new String[]{"gen1"});

        generators = service.getFilteredGenerators(false).collect(Collectors.toList());
        assertThat(generators.size(), is(0));
    }

    @Test
    public void testGeneratorThemes() throws Throwable {
        underTest.startIndexing();
        underTest.startGeneration();
        verify(context, never()).pushTheme(any());
        clearInvocations(context);

        generator1.setTheme("theme1");
        underTest.startIndexing();
        underTest.startGeneration();
        verify(context, times(3)).doWithTheme(any(), any());
        clearInvocations(context);

        generator2.setTheme("theme1");
        underTest.startIndexing();
        underTest.startGeneration();
        verify(context, times(3)).doWithTheme(any(), any());
        clearInvocations(context);

        generator3.setTheme("theme1");
        underTest.startIndexing();
        underTest.startGeneration();
        verify(context, times(3)).doWithTheme(any(), any());
        clearInvocations(context);
    }

    @Test
    public void testFreeableResourcesFreed() throws Throwable {
        underTest.startIndexing();
        underTest.startGeneration();
        verify(mockFreeableResource).free();
    }

}
