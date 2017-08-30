package com.eden.orchid.api.services;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.options.OptionsService;
import com.eden.orchid.api.options.OptionsServiceImpl;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.mockito.Mockito.*;

public final class OptionsServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    private OrchidContext context;
    private OptionsService underTest;
    private OptionsServiceImpl service;

    @Before
    public void testSetup() {

        // test the service directly
        context = mock(OrchidContext.class);
        service = new OptionsServiceImpl();
        service.initialize(context);

        // test that the default implementation is identical to the real implementation
        underTest = new OptionsService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

}
