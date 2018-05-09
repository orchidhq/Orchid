package com.eden.orchid.api.publication;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.options.OptionsExtractor;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PublicationServiceTest {

    private OrchidContext context;
    private Injector injector;
    private OptionsExtractor extractor;

    private PublicationService underTest;
    private PublicationServiceImpl service;

    private Set<OrchidPublisher> publishers;

    private MockPublisher publisher1;
    private MockPublisher publisher2;
    private MockPublisher publisher3;

    @BeforeEach
    public void testSetup() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);
        context = mock(OrchidContext.class);
        injector = mock(Injector.class);
        extractor = mock(OptionsExtractor.class);
        when(context.getInjector()).thenReturn(injector);
        when(injector.getInstance(OptionsExtractor.class)).thenReturn(extractor);

        publishers = new HashSet<>();

        publisher1 = new MockPublisher(context, "pub1", 1000, true, null);
        publisher1 = spy(publisher1);
        publishers.add(publisher1);

        publisher2 = new MockPublisher(context, "pub2", 100, true, null);
        publisher2 = spy(publisher2);
        publishers.add(publisher2);

        publisher3 = new MockPublisher(context, "pub3", 10, true, null);
        publisher3 = spy(publisher3);
        publishers.add(publisher3);

        // test the service directly
        service = new PublicationServiceImpl(publishers);
        service.initialize(context);

        // test that the default implementation is identical to the real implementation
        underTest = new PublicationService() {
            public void initialize(OrchidContext context) { }

            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

    @Test
    public void testSetupCorrectly() throws Throwable {
        underTest.publishAll(true);

        verify(publisher1).extractOptions(any(), any());
        verify(publisher2).extractOptions(any(), any());
        verify(publisher3).extractOptions(any(), any());
    }

    @Test
    public void testFilteringPublishers() throws Throwable {
        service.publishAll(true);

        assertThat(service.getFilteredPublishers(), containsInAnyOrder(publisher1, publisher2, publisher3));

        service.setDisabled(new String[]{"pub1"});
        assertThat(service.getFilteredPublishers(), containsInAnyOrder(publisher2, publisher3));

        service.setDisabled(null);
        assertThat(service.getFilteredPublishers(), containsInAnyOrder(publisher1, publisher2, publisher3));

        service.setEnabled(new String[]{"pub1"});
        assertThat(service.getFilteredPublishers(), containsInAnyOrder(publisher1));

        service.setDisabled(new String[]{"pub1"});
        assertThat(service.getFilteredPublishers().size(), is(0));
    }

    @Test
    public void testPipelineStopsShortWhenStageIsInvalid() throws Throwable {
        publisher2.setValid(false);
        boolean success = underTest.publishAll(false);

        assertThat(success, is(false));
        verify(publisher1).extractOptions(any(), any());
        verify(publisher2).extractOptions(any(), any());
        verify(publisher3, never()).extractOptions(any(), any());
    }

    @Test
    public void testPipelineStopsShortWhenStageThrows() throws Throwable {
        publisher2.setThrownException(new RuntimeException());
        boolean success = underTest.publishAll(false);

        assertThat(success, is(false));
        verify(publisher1).extractOptions(any(), any());
        verify(publisher2).extractOptions(any(), any());
        verify(publisher3, never()).extractOptions(any(), any());
    }

    @Test
    public void testPublishedWhenNotDry() throws Throwable {
        underTest.publishAll(false);
        verify(publisher1).publish();
    }

    @Test
    public void testNotPublishedWhenDry() throws Throwable {
        underTest.publishAll(true);
        verify(publisher1, never()).publish();
    }

    @Test
    public void testNotPublishedWhenPublisherIsDry() throws Throwable {
        publisher1.setDry(true);
        underTest.publishAll(false);
        verify(publisher1, never()).publish();
    }

    @Test
    public void testNotPublishedWhenFailedValidation() throws Throwable {
        publisher1.setValid(false);
        underTest.publishAll(false);
        verify(publisher1, never()).publish();
    }

}
