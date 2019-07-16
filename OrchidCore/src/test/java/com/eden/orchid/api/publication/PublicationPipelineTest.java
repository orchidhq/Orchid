package com.eden.orchid.api.publication;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsExtractor;
import com.eden.orchid.testhelpers.OrchidUnitTest;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class PublicationPipelineTest extends OrchidUnitTest {

    private OrchidContext context;
    private OptionsExtractor extractor;

    private PublicationPipeline underTest;

    private Set<OrchidPublisher> publishers;

    private CrashingPublisher crashingPublisher;
    private InvalidPublisher invalidPublisher;
    private ValidPublisher validPublisher;

    private int progressUpdates;

    // Progress is considered complete when progress == maxProgress
    private boolean didProgressComplete;

    private BiConsumer<Integer, Integer> progressHandler;

    @BeforeEach
    public void setUp() {
        progressUpdates = 0;
        didProgressComplete = false;
        progressHandler = (progress, maxProgress) -> {
            progressUpdates++;
            didProgressComplete = didProgressComplete || (progress.equals(maxProgress));
            Clog.d("Progress: {}/{}", progress, maxProgress);
        };

        context = mock(OrchidContext.class);
        extractor = mock(OptionsExtractor.class);
        when(context.resolve(OptionsExtractor.class)).thenReturn(extractor);

        publishers = new HashSet<>();

        crashingPublisher = new CrashingPublisher();
        invalidPublisher = new InvalidPublisher();
        validPublisher = new ValidPublisher();

        publishers.add(crashingPublisher);
        publishers.add(invalidPublisher);
        publishers.add(validPublisher);

        when(context.resolveSet(OrchidPublisher.class)).thenReturn(publishers);

        crashingPublisher = spy(crashingPublisher);
        invalidPublisher = spy(invalidPublisher);
        validPublisher = spy(validPublisher);

        when(context.resolve(CrashingPublisher.class)).thenReturn(crashingPublisher);
        when(context.resolve(InvalidPublisher.class)).thenReturn(invalidPublisher);
        when(context.resolve(ValidPublisher.class)).thenReturn(validPublisher);

        underTest = new PublicationPipeline(context);
    }

    @Test
    public void testSetupCorrectly() {
        List<Map<String, Object>> stagesJson = new ArrayList<>();
        stagesJson.add(new JSONObject("{\"type\": \"crashing\"}").toMap());
        stagesJson.add(new JSONObject("{\"type\": \"invalid\"}").toMap());
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        underTest.initialize(stagesJson);

        underTest.publishAll(true);

        verify(crashingPublisher, times(1)).validate(context);
        verify(invalidPublisher, times(1)).validate(context);
        verify(validPublisher, times(1)).validate(context);
    }

    @Test
    public void testPipelineStopsShortWhenStageIsInvalid() {
        List<Map<String, Object>> stagesJson = new ArrayList<>();
        stagesJson.add(new JSONObject("{\"type\": \"crashing\"}").toMap());
        stagesJson.add(new JSONObject("{\"type\": \"invalid\"}").toMap());
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll();

        verify(crashingPublisher, times(1)).validate(context);
        verify(invalidPublisher, times(1)).validate(context);
        verify(validPublisher, times(1)).validate(context);

        verify(crashingPublisher, never()).publish(context);
        verify(invalidPublisher, never()).publish(context);
        verify(validPublisher, never()).publish(context);

        assertThat(success, is(false));
    }

    @Test
    public void testPipelineStopsShortWhenStageThrows() {
        List<Map<String, Object>> stagesJson = new ArrayList<>();
        stagesJson.add(new JSONObject("{\"type\": \"crashing\"}").toMap());
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll();

        verify(crashingPublisher, times(1)).validate(context);
        verify(validPublisher, times(1)).validate(context);

        verify(crashingPublisher, times(1)).publish(context);
        verify(validPublisher, times(0)).publish(context);

        assertThat(success, is(false));
    }

    @Test
    public void testPublishedWhenNotDry() {
        List<Map<String, Object>> stagesJson = new ArrayList<>();
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll();

        verify(validPublisher, times(1)).validate(context);
        verify(validPublisher, times(1)).publish(context);

        assertThat(success, is(true));
    }

    @Test
    public void testNotPublishedWhenDry() {
        List<Map<String, Object>> stagesJson = new ArrayList<>();
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll(true);

        verify(validPublisher, times(1)).validate(context);
        verify(validPublisher, times(0)).publish(context);

        assertThat(success, is(true));
    }

    @Test
    public void testNotPublishedWhenPublisherIsDry() {
        List<Map<String, Object>> stagesJson = new ArrayList<>();
        stagesJson.add(new JSONObject("{\"type\": \"valid\", \"dry\": true}").toMap());
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll();

        verify(validPublisher, times(1)).validate(context);
        verify(validPublisher, times(0)).publish(context);

        assertThat(success, is(true));
    }

    @Test
    public void testNotPublishedWhenFailedValidation() {
        List<Map<String, Object>> stagesJson = new ArrayList<>();
        stagesJson.add(new JSONObject("{\"type\": \"invalid\"}").toMap());
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll();

        verify(invalidPublisher, times(1)).validate(context);
        verify(invalidPublisher, times(0)).publish(context);

        assertThat(success, is(false));
    }

    @Test
    public void testProgressUpdatesForValidPublishers() {
        List<Map<String, Object>> stagesJson = new ArrayList<>();
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        underTest.initialize(stagesJson);

        underTest.publishAll(false, progressHandler);

        verify(validPublisher, times(3)).validate(context);

        // updates once at beginning for zero progress, then once for each subsequent progress
        assertThat(progressUpdates, is(equalTo(4)));
        assertThat(didProgressComplete, is(true));
    }

    @Test
    public void testProgressUpdatesForInvalidPublishers() {
        List<Map<String, Object>> stagesJson = new ArrayList<>();
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        stagesJson.add(new JSONObject("{\"type\": \"invalid\"}").toMap());
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        underTest.initialize(stagesJson);

        underTest.publishAll(false, progressHandler);

        verify(validPublisher, times(2)).validate(context);
        verify(invalidPublisher, times(1)).validate(context);

        // updates once at beginning for zero progress, and once for completion, but does not update for each publisher
        assertThat(progressUpdates, is(equalTo(2)));
        assertThat(didProgressComplete, is(true));
    }

    @Test
    public void testProgressUpdatesForCrashingPublishers() {
        List<Map<String, Object>> stagesJson = new ArrayList<>();
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        stagesJson.add(new JSONObject("{\"type\": \"crashing\"}").toMap());
        stagesJson.add(new JSONObject("{\"type\": \"valid\"}").toMap());
        underTest.initialize(stagesJson);

        underTest.publishAll(false, progressHandler);

        verify(validPublisher, times(2)).validate(context);
        verify(crashingPublisher, times(1)).validate(context);

        // updates once at beginning for zero progress, and once for completion, and once for each successful stage
        // deploy, but a progress update is not sent when a stage crashes, as the pipeline exits early and sends the
        // final 'completion' update immediately.
        assertThat(progressUpdates, is(equalTo(2)));
        assertThat(didProgressComplete, is(true));
    }

    public static class MockPublisher extends OrchidPublisher {

        private final boolean isValid;
        private final boolean throwsException;

        public MockPublisher(String key, int priority, boolean isValid, boolean throwsException) {
            super(key, priority);
            this.isValid = isValid;
            this.throwsException = throwsException;
        }

        @Override
        public void extractOptions(OrchidContext context, Map<String, Object> options) {
            setOrder(getPriority());
            setDry(options.get("dry") != null && Boolean.parseBoolean(options.get("dry").toString()));

        }

        @Override
        public boolean validate(OrchidContext context) {
            return isValid;
        }

        @Override
        public void publish(OrchidContext context) {
            if (throwsException) {
                throw new RuntimeException("This MockPublisher throws an exception during publishing");
            }
        }

    }

    public static class ValidPublisher extends MockPublisher {
        public ValidPublisher() {
            super("valid", 1000, true, false);
        }
    }

    public static class InvalidPublisher extends MockPublisher {
        public InvalidPublisher() {
            super("invalid", 100, false, false);
        }
    }

    public static class CrashingPublisher extends MockPublisher {
        public CrashingPublisher() {
            super("crashing", 10, true, true);
        }
    }


}
