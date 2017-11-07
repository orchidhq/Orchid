package com.eden.orchid.api.render;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.google.inject.Injector;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@Test(groups={"services", "unit"})
public final class RenderServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.VERBOSE);
    }

    private Injector injector;
    private OptionsExtractor extractor;
    private OrchidContext context;
    private RenderService underTest;
    private BaseRenderServiceImpl service;
    private TemplateResolutionStrategy templateResolutionStrategy;
    private OrchidPrecompiler precompiler;

    private String resourceContent;
    private OrchidReference reference;
    private OrchidResource resource;
    private OrchidPage page;

    private String layoutContent;
    private OrchidReference layoutReference;
    private OrchidResource layout;

    @BeforeMethod
    public void testSetup() {
        // test the service directly
        context = mock(OrchidContext.class);
        injector = mock(Injector.class);
        extractor = mock(OptionsExtractor.class);
        precompiler = mock(OrchidPrecompiler.class);
        when(injector.getInstance(OptionsExtractor.class)).thenReturn(extractor);

        resourceContent = "test content";
        layoutContent = "test layout";

        when(context.getEmbeddedData(layoutContent)).thenReturn(new EdenPair<>(layoutContent, new JSONElement(new JSONObject())));
        when(context.getEmbeddedData(resourceContent)).thenReturn(new EdenPair<>(resourceContent, new JSONElement(new JSONObject())));
        when(context.getOutputExtension(any())).thenReturn("html");
        when(context.getResourceEntry("one.html")).thenReturn(layout);
        when(context.getInjector()).thenReturn(injector);
        when(injector.getInstance(OrchidPrecompiler.class)).thenReturn(precompiler);

        layoutReference = new OrchidReference(context, "one.html");
        layout = new StringResource(layoutContent, layoutReference);

        reference = new OrchidReference(context, "testContent.html");
        resource = new StringResource(resourceContent, reference);

        page = new OrchidPage(resource, "testContent");
        page = spy(page);
        templateResolutionStrategy = mock(TemplateResolutionStrategy.class);

        when(templateResolutionStrategy.getPageLayout(page)).thenReturn(Collections.singletonList("one.html"));
        when(context.compile("html", layoutContent, page)).thenReturn(layoutContent);
        when(context.compile("html", resourceContent, page)).thenReturn(resourceContent);
        when(context.precompile(layoutContent, page.getData())).thenReturn(layoutContent);
        when(context.precompile(resourceContent, page.getData())).thenReturn(resourceContent);

        service = new BaseRenderServiceImpl(context, templateResolutionStrategy) {
            @Override public boolean render(OrchidPage page, InputStream content) {
                return true;
            }
        };
        service.initialize(context);
        service = spy(service);

        // test that the default implementation is identical to the real implementation
        underTest = new RenderService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

    @Test
    public void testGetRenderedTemplate() throws Throwable {
        InputStream stream = underTest.getRenderedTemplate(page);
        assertThat(stream, is(notNullValue()));

        String content = IOUtils.toString(stream);
        assertThat(content, is(equalTo(layoutContent)));

        verify(page).setCurrent(true);
    }

    @Test
    public void testRenderTemplate() throws Throwable {
        assertThat(underTest.renderTemplate(page), is(true));
    }

    @Test
    public void testGetRenderedString() throws Throwable {
        InputStream stream = underTest.getRenderedString(page, "html", layoutContent);
        assertThat(stream, is(notNullValue()));

        String content = IOUtils.toString(stream);
        assertThat(content, is(equalTo(layoutContent)));

        verify(page).setCurrent(true);
    }

    @Test
    public void testRenderString() throws Throwable {
        assertThat(underTest.renderString(page, "html", layoutContent), is(true));
    }

    @Test
    public void testGetRenderedRaw() throws Throwable {
        InputStream stream = underTest.getRenderedRaw(page);
        assertThat(stream, is(notNullValue()));

        String content = IOUtils.toString(stream);
        assertThat(content, is(equalTo(resourceContent)));

        verify(page).setCurrent(true);
    }

    @Test
    public void testRenderRaw() throws Throwable {
        assertThat(underTest.renderRaw(page), is(true));
    }

    @Test
    public void testGetRenderedBinary() throws Throwable {
        InputStream stream = underTest.getRenderedBinary(page);
        assertThat(stream, is(notNullValue()));

        String content = IOUtils.toString(stream);
        assertThat(content, is(equalTo(resourceContent)));

        verify(page).setCurrent(true);
    }

    @Test
    public void testRenderBinary() throws Throwable {
        assertThat(underTest.renderBinary(page), is(true));

    }



}
