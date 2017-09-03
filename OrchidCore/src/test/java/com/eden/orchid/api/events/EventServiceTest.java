package com.eden.orchid.api.events;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@Test(groups={"services", "unit"})
public final class EventServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    private OrchidContext context;
    private EventService underTest;
    private EventServiceImpl service;

    @BeforeTest
    public void testSetup() {
        EventEmitter emitter = mock(EventEmitter.class);

        // test the service directly
        context = mock(OrchidContext.class);
        service = new EventServiceImpl(emitter);
        service.initialize(context);

        // test that the default implementation is identical to the real implementation
        underTest = new EventService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

    @Test
    public void testTestMethod() throws Throwable {
        assertThat(1, is(equalTo(1)));
    }

}
