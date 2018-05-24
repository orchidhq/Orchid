package com.eden.orchid.api.publication;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.options.OptionsExtractor;
import com.google.inject.Injector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class PublicationPipelineTest {

    private OrchidContext context;
    private Injector injector;
    private OptionsExtractor extractor;

    private PublicationPipeline underTest;

    private Set<OrchidPublisher> publishers;

    private MockPublisher.CrashingPublisher crashingPublisher;
    private MockPublisher.InvalidPublisher invalidPublisher;
    private MockPublisher.ValidPublisher validPublisher;

    private int progressUpdates;

    // Progress is considered complete when progress == maxProgress
    private boolean didProgressComplete;

    private BiConsumer<Integer, Integer> progressHandler;

    @BeforeEach
    public void testSetup() {
        Clog.getInstance().setMinPriority(Clog.Priority.FATAL);

        progressUpdates = 0;
        didProgressComplete = false;
        progressHandler = (progress, maxProgress) -> {
            progressUpdates++;
            didProgressComplete = didProgressComplete || (progress.equals(maxProgress));
            Clog.v("Progress: {}/{}", progress, maxProgress);
        };

        context = mock(OrchidContext.class);
        injector = mock(Injector.class);
        extractor = mock(OptionsExtractor.class);
        when(context.getInjector()).thenReturn(injector);
        when(injector.getInstance(OptionsExtractor.class)).thenReturn(extractor);

        publishers = new HashSet<>();

        crashingPublisher = new MockPublisher.CrashingPublisher(context);
        invalidPublisher = new MockPublisher.InvalidPublisher(context);
        validPublisher = new MockPublisher.ValidPublisher(context);

        publishers.add(crashingPublisher);
        publishers.add(invalidPublisher);
        publishers.add(validPublisher);

        when(context.resolveSet(OrchidPublisher.class)).thenReturn(publishers);

        crashingPublisher = spy(crashingPublisher);
        invalidPublisher = spy(invalidPublisher);
        validPublisher = spy(validPublisher);

        when(injector.getInstance(MockPublisher.CrashingPublisher.class)).thenReturn(crashingPublisher);
        when(injector.getInstance(MockPublisher.InvalidPublisher.class)).thenReturn(invalidPublisher);
        when(injector.getInstance(MockPublisher.ValidPublisher.class)).thenReturn(validPublisher);

        underTest = new PublicationPipeline(context);
    }

    @Test
    public void testSetupCorrectly() throws Throwable {
        JSONArray stagesJson = new JSONArray();
        stagesJson.put(new JSONObject("{\"type\": \"crashing\"}"));
        stagesJson.put(new JSONObject("{\"type\": \"invalid\"}"));
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        underTest.initialize(stagesJson);

        underTest.publishAll(true);

        verify(crashingPublisher, times(1)).validate();
        verify(invalidPublisher,  times(1)).validate();
        verify(validPublisher,    times(1)).validate();
    }

    @Test
    public void testPipelineStopsShortWhenStageIsInvalid() throws Throwable {
        JSONArray stagesJson = new JSONArray();
        stagesJson.put(new JSONObject("{\"type\": \"crashing\"}"));
        stagesJson.put(new JSONObject("{\"type\": \"invalid\"}"));
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll();

        verify(crashingPublisher, times(1)).validate();
        verify(invalidPublisher,  times(1)).validate();
        verify(validPublisher,    times(1)).validate();

        verify(crashingPublisher, never()).publish();
        verify(invalidPublisher,  never()).publish();
        verify(validPublisher,    never()).publish();

        assertThat(success, is(false));
    }

    @Test
    public void testPipelineStopsShortWhenStageThrows() throws Throwable {
        JSONArray stagesJson = new JSONArray();
        stagesJson.put(new JSONObject("{\"type\": \"crashing\"}"));
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll();

        verify(crashingPublisher, times(1)).validate();
        verify(validPublisher,    times(1)).validate();

        verify(crashingPublisher, times(1)).publish();
        verify(validPublisher,    times(0)).publish();

        assertThat(success, is(false));
    }

    @Test
    public void testPublishedWhenNotDry() throws Throwable {
        JSONArray stagesJson = new JSONArray();
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll();

        verify(validPublisher,    times(1)).validate();
        verify(validPublisher,    times(1)).publish();

        assertThat(success, is(true));
    }

    @Test
    public void testNotPublishedWhenDry() throws Throwable {
        JSONArray stagesJson = new JSONArray();
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll(true);

        verify(validPublisher,    times(1)).validate();
        verify(validPublisher,    times(0)).publish();

        assertThat(success, is(true));
    }

    @Test
    public void testNotPublishedWhenPublisherIsDry() throws Throwable {
        JSONArray stagesJson = new JSONArray();
        stagesJson.put(new JSONObject("{\"type\": \"valid\", \"dry\": true}"));
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll();

        verify(validPublisher,    times(1)).validate();
        verify(validPublisher,    times(0)).publish();

        assertThat(success, is(true));
    }

    @Test
    public void testNotPublishedWhenFailedValidation() throws Throwable {
        JSONArray stagesJson = new JSONArray();
        stagesJson.put(new JSONObject("{\"type\": \"invalid\"}"));
        underTest.initialize(stagesJson);

        boolean success = underTest.publishAll();

        verify(invalidPublisher,    times(1)).validate();
        verify(invalidPublisher,    times(0)).publish();

        assertThat(success, is(false));
    }

    @Test
    public void testProgressUpdatesForValidPublishers() throws Throwable {
        JSONArray stagesJson = new JSONArray();
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        underTest.initialize(stagesJson);

        underTest.publishAll(false, progressHandler);

        verify(validPublisher, times(3)).validate();

        // updates once at beginning for zero progress, then once for each subsequent progress
        assertThat(progressUpdates, is(equalTo(4)));
        assertThat(didProgressComplete, is(true));
    }

    @Test
    public void testProgressUpdatesForInvalidPublishers() throws Throwable {
        JSONArray stagesJson = new JSONArray();
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        stagesJson.put(new JSONObject("{\"type\": \"invalid\"}"));
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        underTest.initialize(stagesJson);

        underTest.publishAll(false, progressHandler);

        verify(validPublisher, times(2)).validate();
        verify(invalidPublisher, times(1)).validate();

        // updates once at beginning for zero progress, and once for completion, but does not update for each publisher
        assertThat(progressUpdates, is(equalTo(2)));
        assertThat(didProgressComplete, is(true));
    }

    @Test
    public void testProgressUpdatesForCrashingPublishers() throws Throwable {
        JSONArray stagesJson = new JSONArray();
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        stagesJson.put(new JSONObject("{\"type\": \"crashing\"}"));
        stagesJson.put(new JSONObject("{\"type\": \"valid\"}"));
        underTest.initialize(stagesJson);

        underTest.publishAll(false, progressHandler);

        verify(validPublisher, times(2)).validate();
        verify(crashingPublisher, times(1)).validate();

        // updates once at beginning for zero progress, and once for completion, and once for each successful stage
        // deploy, but a progress update is not sent when a stage crashes, as the pipeline exits early and sends the
        // final 'completion' update immediately.
        assertThat(progressUpdates, is(equalTo(2)));
        assertThat(didProgressComplete, is(true));
    }

}
