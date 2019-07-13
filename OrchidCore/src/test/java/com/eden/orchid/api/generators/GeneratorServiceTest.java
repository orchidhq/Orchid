package com.eden.orchid.api.generators;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.indexing.OrchidRootIndex;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.resources.resource.FreeableResource;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.impl.relations.ThemeRelation;
import com.eden.orchid.testhelpers.BaseOrchidTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class GeneratorServiceTest extends BaseOrchidTest {

    private OrchidContext context;
    private OptionsExtractor extractor;
    private Theme theme;
    private BuildMetrics buildMetrics;

    private GeneratorService underTest;
    private GeneratorServiceImpl service;

    private OrchidRootIndex internalIndex;

    private Set<OrchidGenerator> generators;

    private MockGenerator generator1;
    private List<OrchidPage> pages1;
    private OrchidPage mockPage1;
    private OrchidReference mockPage1Reference;
    private OrchidResource mockPage1Resource;

    private MockGenerator generator2;
    private List<OrchidPage> pages2;
    private FreeableResource mockFreeableResource;
    private OrchidPage mockPage2;
    private OrchidReference mockPage2Reference;

    private MockGenerator generator3;
    private List<OrchidPage> pages3;

    @BeforeEach
    public void setUp() {
        super.setUp();
        context = mock(OrchidContext.class);
        extractor = mock(OptionsExtractor.class);
        theme = mock(Theme.class);
        buildMetrics = new BuildMetrics(context);

        internalIndex = new OrchidRootIndex("internal");

        when(context.resolve(OptionsExtractor.class)).thenReturn(extractor);
        when(context.findTheme(any())).thenReturn(theme);
        when(context.getIndex()).thenReturn(internalIndex);
        when(context.includeDrafts()).thenReturn(false);

        when(theme.isHasRenderedAssets()).thenReturn(true);

        generators = new HashSet<>();

        mockPage1Reference = new OrchidReference(context, "page1.html");
        mockPage1Resource = new StringResource("", mockPage1Reference);
        mockPage1 = spy(new OrchidPage(mockPage1Resource, "mockPage1", ""));
        pages1 = new ArrayList<>();
        pages1.add(mockPage1);
        generator1 = spy(new MockGenerator("gen1", 100, pages1));
        generators.add(generator1);

        mockPage2Reference = new OrchidReference(context, "page2.html");
        mockFreeableResource = spy(new FreeableResource(mockPage2Reference) {});
        mockPage2 = spy(new OrchidPage(mockFreeableResource, "mockPage2", ""));
        pages2 = new ArrayList<>();
        pages2.add(mockPage2);
        generator2 = spy(new MockGenerator("gen2", 150, pages2));
        generators.add(generator2);

        pages3 = new ArrayList<>();
        generator3 = new MockGenerator("gen3", 200, pages3);
        generator3 = spy(generator3);
        generators.add(generator3);

        when(context.resolveSet(OrchidGenerator.class)).thenReturn(generators);

        // test the service directly
        service = new GeneratorServiceImpl();
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

        verify(generator1).extractOptions((OrchidContext) any(), any());
        verify(generator1).startIndexing(context);
        assertThat(generator1.mockPages.size(), is(1));
        assertThat(generator1.mockPages.size(), is(1));

        verify(generator2).extractOptions((OrchidContext) any(), any());
        verify(generator2).startIndexing(context);
        assertThat(generator2.mockPages.size(), is(1));

        verify(generator3).extractOptions((OrchidContext) any(), any());
        verify(generator3).startIndexing(context);
        assertThat(generator3.mockPages, is(notNullValue()));
        assertThat(generator3.mockPages.size(), is(0));
    }

    @Test
    public void testFilteringGenerators() throws Throwable {
        service.startIndexing();
        List<OrchidGenerator> generators;

        generators = service.getFilteredGenerators().collect(Collectors.toList());
        assertThat(generators, containsInAnyOrder(generator1, generator2, generator3));

        service.setDisabled(new String[]{"gen1"});

        generators = service.getFilteredGenerators().collect(Collectors.toList());
        assertThat(generators, containsInAnyOrder(generator2, generator3));

        service.setDisabled(null);

        generators = service.getFilteredGenerators().collect(Collectors.toList());
        assertThat(generators, containsInAnyOrder(generator1, generator2, generator3));

        service.setEnabled(new String[]{"gen1"});

        generators = service.getFilteredGenerators().collect(Collectors.toList());
        assertThat(generators, containsInAnyOrder(generator1));

        service.setDisabled(new String[]{"gen1"});

        generators = service.getFilteredGenerators().collect(Collectors.toList());
        assertThat(generators.size(), is(0));
    }

    @Test
    public void testGeneratorThemes() throws Throwable {
        underTest.startIndexing();
        underTest.startGeneration();
        verify(context, never()).pushTheme(any());
        clearInvocations(context);

        ThemeRelation g1Theme = new ThemeRelation(context);
        g1Theme.extractOptions(context, Collections.singletonMap("key", "theme1"));
        generator1.setTheme(g1Theme);
        underTest.startIndexing();
        underTest.startGeneration();
        verify(context, times(3)).doWithTheme(any(), any());
        clearInvocations(context);

        generator1.setTheme(g1Theme);
        underTest.startIndexing();
        underTest.startGeneration();
        verify(context, times(3)).doWithTheme(any(), any());
        clearInvocations(context);

        generator3.setTheme(g1Theme);
        underTest.startIndexing();
        underTest.startGeneration();
        verify(context, times(3)).doWithTheme(any(), any());
        clearInvocations(context);
    }

    @Test
    public void testFreeableResourcesFreed() throws Throwable {
        doAnswer((Answer) invocation -> {
            mockFreeableResource.free();
            return null;
        }).when(mockPage2).free();

        underTest.startIndexing();
        underTest.startGeneration();
        verify(mockFreeableResource).free();
    }

}
