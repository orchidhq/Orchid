package com.eden.orchid.api.render;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.compilers.OrchidPrecompiler;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.api.resources.resource.OrchidResource;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.api.theme.Theme;
import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.api.theme.pages.OrchidReference;
import com.eden.orchid.testhelpers.OrchidUnitTest;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

public final class RenderServiceTest implements OrchidUnitTest {

    private Theme theme;
    private OptionsExtractor extractor;
    private OrchidContext context;
    private RenderService underTest;
    private RenderServiceImpl service;
    private OrchidPrecompiler precompiler;
    private OrchidRenderer renderer;

    private String resourceContent;
    private OrchidReference reference;
    private OrchidResource resource;
    private OrchidPage page;

    private String layoutContent;
    private OrchidReference layoutReference;
    private OrchidResource layout;

    @BeforeEach
    public void setUp() {
        // test the service directly
        theme = mock(Theme.class);
        context = mock(OrchidContext.class);
        extractor = mock(OptionsExtractor.class);
        precompiler = mock(OrchidPrecompiler.class);
        renderer = mock(OrchidRenderer.class);
        when(context.resolve(OptionsExtractor.class)).thenReturn(extractor);
        when(renderer.render(any(), any())).thenReturn(true);
        when(context.getTheme()).thenReturn(theme);
        when(theme.getPreferredTemplateExtension()).thenReturn("peb");
        when(context.getDefaultTemplateExtension()).thenReturn("peb");

        resourceContent = "test content";
        layoutContent = "test layout";

        when(context.getEmbeddedData("html", layoutContent)).thenReturn(new EdenPair<>(layoutContent, new HashMap<>()));
        when(context.getEmbeddedData("peb",  layoutContent)).thenReturn(new EdenPair<>(layoutContent, new HashMap<>()));
        when(context.getEmbeddedData("html", resourceContent)).thenReturn(new EdenPair<>(resourceContent, new HashMap<>()));
        when(context.getEmbeddedData("peb",  resourceContent)).thenReturn(new EdenPair<>(resourceContent, new HashMap<>()));
        when(context.getOutputExtension(any())).thenReturn("html");
        when(context.resolve(OrchidPrecompiler.class)).thenReturn(precompiler);

        layoutReference = new OrchidReference(context, "one.html");
        layout = new StringResource(layoutContent, layoutReference);

        reference = new OrchidReference(context, "testContent.html");
        resource = new StringResource(resourceContent, reference);

        page = new OrchidPage(resource, "testContent", null);
        page = spy(page);
        page.setPublishDate(LocalDate.now().atStartOfDay());
        page.setExpiryDate(LocalDate.now().atTime(LocalTime.MAX));
        page.setLayout("one.html");

        when(context.locateTemplate("templates/layouts/one.html", true)).thenReturn(layout);
        when(context.compile("html", layoutContent, page)).thenReturn(layoutContent);
        when(context.compile("html", resourceContent, page)).thenReturn(resourceContent);
        when(context.compile("peb", layoutContent, page)).thenReturn(layoutContent);
        when(context.compile("peb", resourceContent, page)).thenReturn(resourceContent);

        service = new RenderServiceImpl(context, renderer);
        service.initialize(context);
        service = spy(service);

        // test that the default implementation is identical to the real implementation
        underTest = new RenderService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

// Test Rendering Template Resource as Layout
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetRenderedTemplate() throws Throwable {
        InputStream stream = underTest.getRenderedTemplate(page);
        assertThat(stream, is(notNullValue()));

        String content = IOUtils.toString(stream, Charset.forName("UTF-8"));
        assertThat(content, is(equalTo(layoutContent)));

        verify(page).setCurrent(true);
    }

    @Test
    public void testRenderTemplate() throws Throwable {
        assertThat(page.isCurrent(), is(false));
        assertThat(underTest.renderTemplate(page), is(true));
        assertThat(page.isCurrent(), is(false));

        verify(page, times(2)).setCurrent(anyBoolean());
        verify(renderer).render(any(), any());
    }

    @Test
    public void testRenderTemplateNotCalledWhenSkipped() throws Throwable {
        page.setDraft(true);
        assertThat(underTest.renderTemplate(page), is(false));
        verify(renderer, never()).render(any(), any());
    }

// Test Rendering Strings as Layouts
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetRenderedString() throws Throwable {
        InputStream stream = underTest.getRenderedString(page, "html", layoutContent);
        assertThat(stream, is(notNullValue()));

        String content = IOUtils.toString(stream, Charset.forName("UTF-8"));
        assertThat(content, is(equalTo(layoutContent)));

        verify(page).setCurrent(true);
    }

    @Test
    public void testRenderString() throws Throwable {
        assertThat(page.isCurrent(), is(false));
        assertThat(underTest.renderString(page, "html", layoutContent), is(true));
        assertThat(page.isCurrent(), is(false));

        verify(page, times(2)).setCurrent(anyBoolean());
        verify(renderer).render(any(), any());
    }

    @Test
    public void testRenderStringNotCalledWhenSkipped() throws Throwable {
        page.setDraft(true);
        assertThat(underTest.renderString(page, "html", layoutContent), is(false));
        verify(renderer, never()).render(any(), any());
    }

// Test Rending Raw Contents without Layout
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetRenderedRaw() throws Throwable {
        InputStream stream = underTest.getRenderedRaw(page);
        assertThat(stream, is(notNullValue()));

        String content = IOUtils.toString(stream, Charset.forName("UTF-8"));
        assertThat(content, is(equalTo(resourceContent)));

        verify(page).setCurrent(true);
    }

    @Test
    public void testRenderRaw() throws Throwable {
        assertThat(page.isCurrent(), is(false));
        assertThat(underTest.renderRaw(page), is(true));
        assertThat(page.isCurrent(), is(false));

        verify(page, times(2)).setCurrent(anyBoolean());
        verify(renderer).render(any(), any());
    }

    @Test
    public void testRenderRawNotCalledWhenSkipped() throws Throwable {
        page.setDraft(true);
        assertThat(underTest.renderRaw(page), is(false));
        verify(renderer, never()).render(any(), any());
    }

// Test Rendering raw contents as binary stream
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testGetRenderedBinary() throws Throwable {
        InputStream stream = underTest.getRenderedBinary(page);
        assertThat(stream, is(notNullValue()));

        String content = IOUtils.toString(stream, Charset.forName("UTF-8"));
        assertThat(content, is(equalTo(resourceContent)));

        verify(page).setCurrent(true);
    }

    @Test
    public void testRenderBinary() throws Throwable {
        assertThat(page.isCurrent(), is(false));
        assertThat(underTest.renderBinary(page), is(true));
        assertThat(page.isCurrent(), is(false));

        verify(page, times(2)).setCurrent(anyBoolean());
        verify(renderer).render(any(), any());
    }

    @Test
    public void testRenderBinaryNotCalledWhenSkipped() throws Throwable {
        page.setDraft(true);
        assertThat(underTest.renderBinary(page), is(false));
        verify(renderer, never()).render(any(), any());
    }

// Test RenderService utility methods
//----------------------------------------------------------------------------------------------------------------------

    @Test
    public void testToStream() throws Throwable {
        assertThat(service.toStream(resourceContent), is(notNullValue()));
        assertThat(service.toStream(null), is(nullValue()));
    }

    @Test
    public void testSkipPage() throws Throwable {
        assertThat(service.skipPage(page), is(false));

        service.dry = true;
        assertThat(service.skipPage(page), is(true));

        service.dry = false;
        page.setDraft(true);
        assertThat(service.skipPage(page), is(true));

        service.includeDrafts = true;
        assertThat(service.skipPage(page), is(false));
    }

    @Test
    public void testRenderingSkippedInDryRun() throws Throwable {
        assertThat(service.skipPage(page), is(false));
        service.dry = true;
        assertThat(service.skipPage(page), is(true));

        assertThat(underTest.renderBinary(page), is(false));
        verify(renderer, never()).render(any(), any());
    }

    @Test
    public void testDraftSkipped() throws Throwable {
        assertThat(service.skipPage(page), is(false));
        page.setDraft(true);
        assertThat(service.skipPage(page), is(true));

        assertThat(underTest.renderBinary(page), is(false));
        verify(renderer, never()).render(any(), any());
    }

    @Test
    public void testDraftNotSkippedWhenRenderingDrafts() throws Throwable {
        assertThat(service.skipPage(page), is(false));
        page.setDraft(true);
        assertThat(service.skipPage(page), is(true));
        service.includeDrafts = true;
        assertThat(service.skipPage(page), is(false));

        assertThat(underTest.renderBinary(page), is(true));
        verify(renderer, times(1)).render(any(), any());
    }

    @Test
    public void testDraftVariableSkipped() throws Throwable {
        assertThat(service.skipPage(page), is(false));
        page.setDraft(true);
        assertThat(service.skipPage(page), is(true));

        assertThat(underTest.renderBinary(page), is(false));
        verify(renderer, never()).render(any(), any());
    }

    @Test
    public void testFuturePublishDateSkipped() throws Throwable {
        assertThat(service.skipPage(page), is(false));
        page.setPublishDate(LocalDate.now().plusDays(1).atTime(LocalTime.MAX));
        assertThat(service.skipPage(page), is(true));

        assertThat(underTest.renderBinary(page), is(false));
        verify(renderer, never()).render(any(), any());
    }

    @Test
    public void testPastExpiryDateSkipped() throws Throwable {
        assertThat(service.skipPage(page), is(false));
        page.setExpiryDate(LocalDate.now().minusDays(1).atStartOfDay());
        assertThat(service.skipPage(page), is(true));

        assertThat(underTest.renderBinary(page), is(false));
        verify(renderer, never()).render(any(), any());
    }


}
