package com.eden.orchid.api.options;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.testhelpers.BaseOrchidTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;

public final class OptionsServiceTest extends BaseOrchidTest {

    private Set<TemplateGlobal> globals;
    private OrchidContext context;
    private OptionsService underTest;
    private OptionsServiceImpl service;

    @BeforeEach
    public void setUp() {
        super.setUp();
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
