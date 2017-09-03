package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

@Test(groups={"services", "unit"}, dependsOnGroups = {"options"})
public final class OptionsServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    private OrchidContext context;
    private OptionsService underTest;
    private OptionsServiceImpl service;

    @BeforeTest
    public void testSetup() {

        // test the service directly
        context = mock(OrchidContext.class);
        service = new OptionsServiceImpl("test");
        service.initialize(context);

        // test that the default implementation is identical to the real implementation
        underTest = new OptionsService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

}
