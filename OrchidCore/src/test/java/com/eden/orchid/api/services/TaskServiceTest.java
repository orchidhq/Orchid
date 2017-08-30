package com.eden.orchid.api.services;

import com.caseyjbrooks.clog.Clog;
import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.generators.OrchidGenerators;
import com.eden.orchid.api.server.FileWatcher;
import com.eden.orchid.api.server.OrchidServer;
import com.eden.orchid.api.tasks.OrchidTask;
import com.eden.orchid.api.tasks.TaskService;
import com.eden.orchid.api.tasks.TaskServiceImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public final class TaskServiceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Clog.setMinPriority(Clog.Priority.FATAL);
    }

    private OrchidContext context;
    private TaskService underTest;
    private TaskServiceImpl service;

    @Before
    public void testSetup() {
        Set<OrchidTask> tasks = new HashSet<>();
        OrchidTask task1 = mock(OrchidTask.class);
        when(task1.getName()).thenReturn("task1");
        tasks.add(task1);

        OrchidTask task2 = mock(OrchidTask.class);
        when(task2.getName()).thenReturn("task2");
        tasks.add(task2);

        OrchidGenerators generators = mock(OrchidGenerators.class);
        OrchidServer server = mock(OrchidServer.class);
        FileWatcher fileWatcher = mock(FileWatcher.class);

        // test the service directly
        context = mock(OrchidContext.class);
        service = new TaskServiceImpl(tasks, generators, "", "", server, fileWatcher);
        service.initialize(context);

        // test that the default implementation is identical to the real implementation
        underTest = new TaskService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

    @Test
    public void run() throws Throwable {
        assertThat(underTest.run("task1"), is(true));
        assertThat(underTest.run("task2"), is(true));
        assertThat(underTest.run("task3"), is(false));
    }

    @Test
    public void build() throws Throwable {
        // test .build by testing individual objects
    }

    @Test
    public void watch() throws Throwable {
        // test .watch by testing FileWatcher
    }

    @Test
    public void serve() throws Throwable {
        // test .build by testing server
    }

}
