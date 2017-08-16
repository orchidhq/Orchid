package com.eden.orchid.api.tasks;

import com.eden.orchid.api.OrchidService;

public interface TaskService extends OrchidService {

    default boolean run(String taskName) {
        return getService(TaskService.class).run(taskName);
    }

    default void build() {
        getService(TaskService.class).build();
    }

    default void serve() {
        getService(TaskService.class).serve();
    }
}
