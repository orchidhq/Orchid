package com.eden.orchid.api.options;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

@Test(groups={"services", "unit"}, dependsOnGroups = {"options"})
public final class OptionsServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
    }

    private Set<TemplateGlobal> globals;
    private OrchidContext context;
    private OptionsService underTest;
    private OptionsServiceImpl service;

    @BeforeMethod
    public void testSetup() {

        // test the service directly
        context = mock(OrchidContext.class);
        globals = new HashSet<>();
        service = new OptionsServiceImpl(globals);
        service.initialize(context);

        // test that the default implementation is identical to the real implementation
        underTest = new OptionsService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

}
