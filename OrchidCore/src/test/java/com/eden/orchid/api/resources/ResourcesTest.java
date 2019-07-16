package com.eden.orchid.api.resources;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.ResourceWrapper;
import com.eden.orchid.api.resources.resource.StringResource;
import com.eden.orchid.testhelpers.OrchidUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public final class ResourcesTest extends OrchidUnitTest {

    private String input;

    private OrchidContext context;

    @BeforeEach
    public void setUp() {
        // test the service directly
        context = mock(OrchidContext.class);
        input = "hello world";

        when(context.getEmbeddedData("", input)).thenReturn(new EdenPair<>(input, new HashMap<>()));
        when(context.getEmbeddedData("txt", input)).thenReturn(new EdenPair<>(input, new HashMap<>()));
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
        ResourceTransformation transformation = new ResourceTransformation(stringResource, Arrays.asList(String::toUpperCase));

        assertThat(transformation.getContent(), is(equalTo("HELLO WORLD")));
    }

    @Test
    public void testResourceTransformationManyTransforms() {
        StringResource stringResource = new StringResource(context, "hello-world.txt", input);
        ResourceTransformation transformation = new ResourceTransformation(stringResource, Arrays.asList(String::toUpperCase, s -> s.replaceAll("\\s+", "-")));

        assertThat(transformation.getContent(), is(equalTo("HELLO-WORLD")));
    }

}
