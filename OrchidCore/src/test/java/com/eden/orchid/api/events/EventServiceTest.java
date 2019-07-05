package com.eden.orchid.api.events;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.testhelpers.BaseOrchidTest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

public final class EventServiceTest extends BaseOrchidTest {

    private OrchidContext context;
    private EventService underTest;
    private EventServiceImpl service;

    private Object sender;
    private OrchidEvent baseEvent;
    private TestEvent specificEvent;
    private TestEvent3 subclassEvent;
    private NestedEvent nestedEvent;
    private EventThatThrowsException eventThatThrowsException;
    private EventThatFiresNewValidEvent eventThatFiresNewValidEvent;

    private TestEventListener listener;
    private Set<OrchidEventListener> listeners;

    private String[] validCallbackNames = new String[]{
            "callbackOne",
            "callbackTwo",
            "callbackThree",
            "callbackFive",
            "callbackSix",
            "callbackSeven",
            "callbackEleven",
            "callbackTwelve",
            "callbackThirteen"
    };

    @BeforeEach
    public void setUp() {
        super.setUp();
        sender = new Object();
        baseEvent = new TestEvent2(sender);
        specificEvent = new TestEvent(sender);
        subclassEvent = new TestEvent3(sender);
        nestedEvent = new NestedEvent(sender);
        eventThatThrowsException = new EventThatThrowsException(sender);
        eventThatFiresNewValidEvent = new EventThatFiresNewValidEvent(sender);

        listener = spy(new TestEventListener());

        listeners = new HashSet<>();
        listeners.add(listener);

        // test the service directly
        context = mock(OrchidContext.class);
        service = new EventServiceImpl(listeners);
        service.initialize(context);

        // test that the default implementation is identical to the real implementation
        underTest = new EventService() {
            public void initialize(OrchidContext context) { }

            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

    @Test
    public void testSetupCorrectly() throws Throwable {
        assertThat(service.getEventHandlers().size(), is(equalTo(validCallbackNames.length)));

        List<String> callbackNames = service
                .getEventHandlers()
                .stream()
                .map(eventHandler -> eventHandler.getCallback().getName())
                .collect(Collectors.toList());

        assertThat(callbackNames, containsInAnyOrder(validCallbackNames));
        assertThat(specificEvent.toString(), is("TestEvent"));
    }

    @Test
    public void testDynamicSetup() throws Throwable {
        assertThat(service.getEventHandlers().size(), is(equalTo(validCallbackNames.length)));

        DynamicEventListener dynamicEventListener = new DynamicEventListener();

        underTest.registerEventListeners(dynamicEventListener);
        assertThat(service.getEventHandlers().size(), is(equalTo(validCallbackNames.length + 1)));

        underTest.deregisterEventListeners(dynamicEventListener);
        assertThat(service.getEventHandlers().size(), is(equalTo(validCallbackNames.length)));
    }

    @Test
    public void broadcastBaseClass() throws Throwable {
        underTest.broadcast(baseEvent);

        verify(listener, never()).callbackOne(any());
        verify(listener, never()).callbackTwo(any());
        verify(listener, never()).callbackThree(any());
        verify(listener, never()).callbackFour();
        verify(listener).callbackFive(baseEvent);
        verify(listener, never()).callbackSix(any());
        verify(listener, never()).callbackSeven(any());

        assertThat(baseEvent.getSender(), is(sender));
    }

    @Test
    public void broadcastSpecificClass() throws Throwable {
        underTest.broadcast(specificEvent);

        verify(listener).callbackOne(specificEvent);
        verify(listener, never()).callbackTwo(any());
        verify(listener).callbackThree(specificEvent);
        verify(listener, never()).callbackFour();
        verify(listener).callbackFive(specificEvent);
        verify(listener, never()).callbackSix(any());
        verify(listener, never()).callbackSeven(any());
    }

    @Test
    public void broadcastSubclassClass() throws Throwable {
        underTest.broadcast(subclassEvent);

        verify(listener, never()).callbackOne(any());
        verify(listener, never()).callbackTwo(any());
        verify(listener).callbackThree(subclassEvent);
        verify(listener, never()).callbackFour();
        verify(listener).callbackFive(subclassEvent);
        verify(listener).callbackSix(subclassEvent);
        verify(listener).callbackSeven(subclassEvent);
    }

    @Test
    public void blockNestedEvents() throws Throwable {
        try {
            underTest.broadcast(nestedEvent);
        }
        catch (RuntimeException e) {
            assertThat(ExceptionUtils.getRootCause(e) instanceof IllegalStateException, is(true));
        }
    }

    @Test
    public void bubbleCallbackExceptions() throws Throwable {
        try {
            underTest.broadcast(eventThatThrowsException);
        }
        catch (RuntimeException e) {
            assertThat(ExceptionUtils.getRootCause(e) instanceof NullPointerException, is(true));
        }
    }

    @Test
    public void allowDifferentNestedEvents() throws Throwable {
        underTest.broadcast(eventThatFiresNewValidEvent);

        verify(listener).callbackThirteen(eventThatFiresNewValidEvent);

        // inner event fires TestEvent, which should hit this callback
        verify(listener).callbackOne(any());
    }

// Classes used for testing
//----------------------------------------------------------------------------------------------------------------------

    private static class TestEvent extends OrchidEvent {
        public TestEvent(Object sender) {
            super(sender);
        }
    }

    private static class TestEvent2 extends OrchidEvent {
        public TestEvent2(Object sender) {
            super(sender);
        }
    }

    private static class TestEvent3 extends TestEvent {
        public TestEvent3(Object sender) {
            super(sender);
        }
    }

    private static class NestedEvent extends OrchidEvent {
        public NestedEvent(Object sender) {
            super(sender);
        }
    }

    private static class EventThatThrowsException extends OrchidEvent {
        public EventThatThrowsException(Object sender) {
            super(sender);
        }
    }

    private static class EventThatFiresNewValidEvent extends OrchidEvent {
        public EventThatFiresNewValidEvent(Object sender) {
            super(sender);
        }
    }

    private class TestEventListener implements OrchidEventListener {

        @On(TestEvent.class)
        public void callbackOne(TestEvent event) {
            // declares and receives a discrete Event class
        }

        @On(OrchidEvent.class)
        public void callbackTwo(OrchidEvent event) {
            // declares and receives a discrete Event class that is the base class
        }

        @On(value = OrchidEvent.class, subclasses = true)
        public void callbackThree(TestEvent event) {
            // declares base Event class but receives specific type
        }

        @On(OrchidEvent.class)
        public void callbackFour() {
            // invalid callback method, not added
        }

        @On(value = OrchidEvent.class, subclasses = true)
        public void callbackFive(OrchidEvent event) {
            // declares base Event class but and receives any type
        }

        @On(TestEvent3.class)
        public void callbackSix(TestEvent3 event) {
            // declares and receives a discrete Event class
        }

        @On(value = TestEvent.class, subclasses = true)
        public void callbackSeven(TestEvent3 event) {
            // declares and receives a discrete Event class
        }

        @On(value = OrchidEvent.class, subclasses = true)
        public void callbackEight() {
            // invalid callback method, not added
        }

        @On(value = TestEvent.class, subclasses = true)
        public void callbackNine(TestEvent2 event) {
            // invalid callback method, not added
        }

        @On(TestEvent.class)
        public void callbackTen(TestEvent2 event) {
            // invalid callback method, not added
        }

        @On(NestedEvent.class)
        public void callbackEleven(NestedEvent event) {
            // callback fires another event of the same class, which is not allowed
            underTest.broadcast(new NestedEvent(this));
        }

        @On(EventThatThrowsException.class)
        public void callbackTwelve(EventThatThrowsException event) {
            // callback throws an exception, which should bubble up to the caller
            throw new NullPointerException();
        }

        @On(EventThatFiresNewValidEvent.class)
        public void callbackThirteen(EventThatFiresNewValidEvent event) {
            // callback fires a new event that is not of the same type, and so is valid
            underTest.broadcast(new TestEvent(this));
        }
    }

    private class DynamicEventListener implements OrchidEventListener {
        @On(TestEvent.class)
        public void callbackOne(TestEvent event) {
            // declares and receives a discrete Event class
        }
    }

}
