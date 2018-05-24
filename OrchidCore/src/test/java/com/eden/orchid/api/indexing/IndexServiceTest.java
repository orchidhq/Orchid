package com.eden.orchid.api.indexing;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.*;

public final class IndexServiceTest {

    private OrchidContext context;
    private IndexService underTest;
    private IndexServiceImpl service;

    @BeforeEach
    public void testSetup() {

        // test the service directly
        context = mock(OrchidContext.class);
        service = new IndexServiceImpl();
        service.initialize(context);

        // test that the default implementation is identical to the real implementation
        underTest = new IndexService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

}
