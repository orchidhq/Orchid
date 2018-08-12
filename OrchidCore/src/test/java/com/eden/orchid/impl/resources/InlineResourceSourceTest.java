package com.eden.orchid.impl.resources;

import com.eden.common.util.EdenPair;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.resources.resource.OrchidResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

public class InlineResourceSourceTest {

    private OrchidContext context;
    private InlineResourceSource underTest;

    @BeforeEach
    public void setup() {
        context = mock(OrchidContext.class);
        underTest = new InlineResourceSource(context);
    }

    @Test
    public void testInlineResourceSource() {
        String input = "inline:extra.scss:This is my content";
        String expected = "This is my content";

        when(context.getEmbeddedData(anyString(), anyString())).thenReturn(new EdenPair<>(expected, new HashMap<>()));
        when(context.getOutputExtension("scss")).thenReturn("css");

        OrchidResource output = underTest.getResourceEntry(input);

        assertThat(output, is(notNullValue()));
        assertThat(output.getContent(), is(equalTo(expected)));
        assertThat(output.getReference().getExtension(), is(equalTo("scss")));
        assertThat(output.getReference().getOutputExtension(), is(equalTo("css")));
    }


}
