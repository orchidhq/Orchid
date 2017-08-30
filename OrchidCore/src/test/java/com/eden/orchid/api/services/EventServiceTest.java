package com.eden.orchid.api.services;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.events.EventEmitter;
import com.eden.orchid.api.events.EventService;
import com.eden.orchid.api.events.EventServiceImpl;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.mockito.Mockito.*;

public final class EventServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    private OrchidContext context;
    private EventService underTest;
    private EventServiceImpl service;

    @Before
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

}
