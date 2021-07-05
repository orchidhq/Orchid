package com.eden.orchid.api.tasks;

import com.eden.orchid.api.OrchidService;
import com.google.inject.ImplementedBy;

@ImplementedBy(TaskServiceImpl.class)
public interface TaskService extends OrchidService {

    public enum TaskType {
        BUILD, WATCH, SERVE, DEPLOY, OTHER
    }

    default void initOptions() {
        getService(TaskService.class).initOptions();
    }

    default boolean runTask(String taskName) {
        return getService(TaskService.class).runTask(taskName);
    }

    default boolean runCommand(String input) {
        return getService(TaskService.class).runCommand(input);
    }

    default void build() {
        getService(TaskService.class).build();
    }

    default void watch() {
        getService(TaskService.class).watch();
    }

    default void serve() {
        getService(TaskService.class).serve();
    }

    default boolean deploy(boolean dryDeploy) {
        return getService(TaskService.class).deploy(dryDeploy);
    }

    default TaskType getTaskType() {
        return getService(TaskService.class).getTaskType();
    }
}
