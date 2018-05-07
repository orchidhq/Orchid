package com.eden.orchid.api.resources;

import com.caseyjbrooks.clog.Clog;
import com.eden.common.json.JSONElement;
import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.ResourceTransformation;
import com.eden.orchid.api.resources.resource.ResourceWrapper;
import com.eden.orchid.api.resources.resource.StringResource;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@Test(groups={"services", "unit"})
public final class ResourcesTest {

    private String input;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

    private OrchidContext context;

    @BeforeMethod
    public void testSetup() {
        // test the service directly
        context = mock(OrchidContext.class);
        input = "hello world";

        when(context.getEmbeddedData(input)).thenReturn(new EdenPair<>(input, new JSONElement(new JSONObject())));
    }

    @Test
    public void testResourceWrapper() {
        StringResource stringResource = new StringResource(context, "hello-world.txt", input);
        ResourceWrapper wrapper = new ResourceWrapper(stringResource);

        assertThat(wrapper.getContent(), is(equalTo(input)));
    }

    @Test
    public void testResourceTransformationNoTransforms() {
        StringResource stringResource = new StringResource(context, "hello-world.txt", input);
        ResourceTransformation transformation = new ResourceTransformation(stringResource);

        assertThat(transformation.getContent(), is(equalTo("hello world")));
    }

    @Test
    public void testResourceTransformationOneTransform() {
        StringResource stringResource = new StringResource(context, "hello-world.txt", input);
        ResourceTransformation transformation = new ResourceTransformation(stringResource, String::toUpperCase);

        assertThat(transformation.getContent(), is(equalTo("HELLO WORLD")));
    }

    @Test
    public void testResourceTransformationManyTransforms() {
        StringResource stringResource = new StringResource(context, "hello-world.txt", input);
        ResourceTransformation transformation = new ResourceTransformation(stringResource, String::toUpperCase, s -> s.replaceAll("\\s+", "-"));

        assertThat(transformation.getContent(), is(equalTo("HELLO-WORLD")));
    }

}
