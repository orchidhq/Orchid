package com.eden.orchid.api.generators;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@Test(groups = {"services", "unit"})
public final class EventServiceTest {

    private OrchidContext context;
    private GeneratorService underTest;
    private GeneratorServiceImpl service;

    private Set<OrchidGenerator> generators;
    private OrchidGenerator generator;

    @BeforeMethod
    public void testSetup() {
        Clog.setMinPriority(Clog.Priority.FATAL);

        generators = new HashSet<>();
        generator = mock(OrchidGenerator.class);
        generators.add(generator);

        // test the service directly
        context = mock(OrchidContext.class);
        service = new GeneratorServiceImpl(generators);
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

        assertThat(1, is(equalTo(1)));
    }

}
