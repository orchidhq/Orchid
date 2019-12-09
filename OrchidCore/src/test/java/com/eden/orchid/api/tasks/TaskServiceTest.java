package com.eden.orchid.api.tasks;

import com.eden.orchid.api.OrchidContext;
import com.eden.orchid.api.OrchidService;
import com.eden.orchid.api.server.FileWatcher;
import com.eden.orchid.api.server.OrchidServer;
import com.eden.orchid.testhelpers.OrchidUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public final class TaskServiceTest implements OrchidUnitTest {

    private OrchidContext context;
    private TaskService underTest;
    private TaskServiceImpl service;

    @BeforeEach
    public void setUp() {
        Set<OrchidTask> tasks = new HashSet<>();
        OrchidTask task1 = mock(OrchidTask.class);
        when(task1.getName()).thenReturn("task1");
        tasks.add(task1);

        OrchidTask task2 = mock(OrchidTask.class);
        when(task2.getName()).thenReturn("task2");
        tasks.add(task2);

        Set<OrchidCommand> commands = new HashSet<>();

        OrchidServer server = mock(OrchidServer.class);
        FileWatcher fileWatcher = mock(FileWatcher.class);

        // test the service directly
        context = mock(OrchidContext.class);
        service = new TaskServiceImpl(tasks, commands, "", "", 8080, server, fileWatcher);
        service.initialize(context);

        // test that the default implementation is identical to the real implementation
        underTest = new TaskService() {
            public void initialize(OrchidContext context) { }
            public <T extends OrchidService> T getService(Class<T> serviceClass) { return (T) service; }
        };
    }

    @Test
    public void run() throws Throwable {
        assertThat(underTest.runTask("task1"), is(true));
        assertThat(underTest.runTask("task2"), is(true));
        assertThat(underTest.runTask("task3"), is(false));
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
        // test .serve by testing server
    }

}
